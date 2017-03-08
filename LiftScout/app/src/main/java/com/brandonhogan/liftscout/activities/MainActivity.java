package com.brandonhogan.liftscout.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.constants.DefaultScreens;
import com.brandonhogan.liftscout.core.managers.NavigationManager;
import com.brandonhogan.liftscout.core.managers.NotificationServiceManager;
import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.core.utils.LogUtil;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.DatabaseRealm;
import com.brandonhogan.liftscout.views.calendar.CalendarFragment;
import com.brandonhogan.liftscout.views.home.HomeFragment;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends BaseActivity implements NavigationManager.NavigationListener {

    @Bind(R.id.bottom_navigation)
    BottomNavigationViewEx bottomNav;

    // Private Static Properties
    //
    private static final String SAVE_STATE_TODAY_PROGRESS_DATE = "saveStateTodayProgressDate";


    // Private Properties
    //
    private NavigationManager navigationManager;
    private SweetAlertDialog dialog;
    private Toolbar toolbar;

    @Inject
    ProgressManager progressManager;

    @Inject
    DatabaseRealm databaseRealm;

    // Overrides
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        Injector.getAppComponent().inject(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.fragment_manager) != null) {

            navigationManager = new NavigationManager();
            navigationManager.init(getFragmentManager());
            navigationManager.setNavigationListener(this);

            setupBottomNavigation(savedInstanceState);

            //This is set when restoring from a previous state,
            //so we do not want to try and load a new fragment
            if (savedInstanceState != null) {
                Date todayDate = (Date)savedInstanceState.getSerializable(SAVE_STATE_TODAY_PROGRESS_DATE);
                progressManager.setTodayProgress(todayDate);
                return;
            }

            Bundle bundle = getIntent().getExtras();

            if (bundle != null) {

                LogUtil.printIntents(bundle, getTAG());
                String notificationId = (String) bundle.get(NotificationServiceManager.NOTIFICATION_ID);

                if (notificationId != null) {
                    switch (notificationId) {
                        case NotificationServiceManager.REST_TIMER_TRIGGER_NOTIFICATION:
                            progressManager.setTodayProgress((long) bundle.get(NotificationServiceManager.REST_TIMER_TRIGGER_DATE));
                            navigationManager.startWorkoutContainer((int) bundle.get(NotificationServiceManager.REST_TIMER_TRIGGER_EXERCISE_ID), (int) bundle.get(NotificationServiceManager.REST_TIMER_TRIGGER_TIME));
                            break;
                    }
                    return;
                }
            }

            // If no notifications or any other kind of bundle was set, we will default to calendar or today
            if (userManager.getHomeDefaultValue().equals(DefaultScreens.CALENDAR)) {
                navigationManager.startCalendar();
                return;
            }
            navigationManager.startToday();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserData();
        navigationManager.setNavigationListener(this);
        navigationManager.setmFragmentManager(getFragmentManager());
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateUserData();
        navigationManager.setNavigationListener(null); // Prevent memory leak on recreate
        navigationManager.setmFragmentManager(null);
    }

    @Override
    public void onDestroy() {
        databaseRealm.close();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SAVE_STATE_TODAY_PROGRESS_DATE,
                progressManager.getTodayProgress().getDate());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 1) {
            // we have only one fragment left so we would close the application with this back
            showExitDialog();
        } else {
            navigationManager.navigateBack(this);
        }
    }

    // Public Functions
    //
    public void setTitle(String title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    public NavigationManager getNavigationManager() {
        return navigationManager;
    }


    // Private Functions
    //

    private void setupBottomNavigation(Bundle savedInstanceState) {
        navigationManager.setBottomNav(bottomNav);

        bottomNav.enableAnimation(false);
        bottomNav.enableShiftingMode(false);
        bottomNav.enableItemShiftingMode(false);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_about:
                        getNavigationManager().startAbout();
                        return true;
                    case R.id.nav_settings:
                        getNavigationManager().startSettings();
                        return true;
                    case R.id.nav_exercises:
                        getNavigationManager().startCategoryList();
                        return true;
                    case R.id.nav_graphs:
                        getNavigationManager().startGraphsContainer(false);
                        return true;
                }

                return false;
            }
        });
    }

    @Override
    public void onBackstackChanged() {

        Fragment f = getFragmentManager().findFragmentById(R.id.fragment_manager);

        if (f instanceof CalendarFragment) {
            getNavigationManager().showBottomNav();
        }
        else if (f instanceof HomeFragment) {
            getNavigationManager().showBottomNav();
        }
        else
            getNavigationManager().hideBottomNav();
    }

    private void updateUserData() {
        userManager.lastUsed(new Date());
    }

    private void showExitDialog() {

        dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.dialog_close_app_title))
                .setContentText(getString(R.string.dialog_close_app_message))
                .setConfirmText(getString(R.string.yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        endActivity();
                    }
                })
                .setCancelText(getString(R.string.cancel))
                .showCancelButton(true);

        dialog.show();
    }

    private void endActivity() {
        if (dialog != null)
            dialog.dismiss();

        finish();
    }
}
