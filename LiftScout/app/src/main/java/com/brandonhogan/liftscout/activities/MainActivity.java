package com.brandonhogan.liftscout.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.constants.DefaultScreens;
import com.brandonhogan.liftscout.core.managers.NavigationManager;
import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.DatabaseRealm;

import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        NavigationManager.NavigationListener {

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;

    @Bind(R.id.nav_view)
    NavigationView navigationView;


    // Private Static Properties
    //
    private static final String SAVE_STATE_TODAY_PROGRESS_DATE = "saveStateTodayProgressDate";


    // Private Properties
    //
    private NavigationManager navigationManager;
    private ActionBarDrawerToggle toggle;
    private SweetAlertDialog dialog;
    private Toolbar toolbar;
    private int timerInterval;

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

             toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            navigationView.setNavigationItemSelectedListener(this);

            navigationManager = new NavigationManager();
            navigationManager.init(getFragmentManager(), drawer);
            navigationManager.setNavigationListener(this);

            //This is set when restoring from a previous state,
            //so we do not want to try and load a new fragment
            if (savedInstanceState != null) {
                Date todayDate = (Date)savedInstanceState.getSerializable(SAVE_STATE_TODAY_PROGRESS_DATE);
                progressManager.setTodayProgress(todayDate);
                setDrawerIndicator();
                return;
            }

            // This needs to be hit first regardless of where a notification will go. Home must
            // be the first item in the back stack

            if (userManager.getHomeDefaultValue().equals(DefaultScreens.CALENDAR)) {
                navigationManager.startCalendar();
            }
            else {
                navigationManager.startToday();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserData();
        navigationManager.setNavigationListener(this);
        navigationManager.setDrawer(drawer);
        navigationManager.setmFragmentManager(getFragmentManager());
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateUserData();
        navigationManager.setNavigationListener(null); // Prevent memory leak on recreate
        navigationManager.setDrawer(null);
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
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (getFragmentManager().getBackStackEntryCount() == 1) {
            // we have only one fragment left so we would close the application with this back
            showExitDialog();
        } else {
            navigationManager.navigateBack(this);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        boolean success = false;

        if (id == R.id.nav_today) {
            success = navigationManager.startToday();
        } else if (id == R.id.nav_calendar) {
            progressManager.setTodayProgress(new Date());
            success = navigationManager.startCalendar();
        } else if (id == R.id.nav_exercises) {
            success = navigationManager.startCategoryList();

//        } else if (id == R.id.nav_routines) {

        } else if (id == R.id.nav_graphs) {
            success = navigationManager.startGraphsContainer(false);
        } else if (id == R.id.nav_settings) {
            success = navigationManager.startSettings();
        } else if (id == R.id.nav_about) {
            success = navigationManager.startAbout();
        }
//        else if (id == R.id.nav_realm) {
//            DatabaseOutput.SendRealmToPhone(this);
//            success = true;
//        }

        if (success)
            drawer.closeDrawer(GravityCompat.START);

        return success;
    }


    // Public Functions
    //
    public void setTitle(String title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

//
//        io.reactivex.Observable.interval(0, 1, TimeUnit.SECONDS)
//                .timeInterval()
//                .observeOn(AndroidSchedulers.mainThread())
//                .take(interval)
//                .doAfterTerminate(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        view.onRestTimerTerminate(exerciseTrackVibrate);
//                        disposable.dispose();
//                    }
//                })
//                .subscribe(new Consumer<Timed<Long>>() {
//                    @Override
//                    public void accept(@NonNull Timed<Long> longTimed) throws Exception {
//                        exerciseTimerTracked -= 1;
//                        view.onRestTimerTick(exerciseTimerTracked);
//                    }
//                });
//    }

    public NavigationManager getNavigationManager() {
        return navigationManager;
    }


    // Private Functions
    //
    private void updateUserData() {

        //TODO : Replace once userManager is fully DI

//        getRealm().beginTransaction();
//        User user = getRealm().where(User.class).findFirst();
//        user.setLastUsed(new Date());
//        getRealm().commitTransaction();
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



    // Navigation Manager Callbacks
    @Override
    public void onBackstackChanged() {
        String fragment = navigationManager.getCurrentFragment().getClass().getSimpleName();

        switch (fragment) {
            case "HomeFragment":
                navigationView.setCheckedItem(R.id.nav_today);
                break;
            case "CalendarFragment":
                navigationView.setCheckedItem(R.id.nav_calendar);
                break;
            case "CategoryListFragment":
                navigationView.setCheckedItem(R.id.nav_exercises);
                break;
//            case "RoutineListFragment":
//                navigationView.setCheckedItem(R.id.nav_routines);
//                break;
            case "GraphsFragment":
                navigationView.setCheckedItem(R.id.nav_graphs);
                break;
            case "SettingsListFragment":
                navigationView.setCheckedItem(R.id.nav_settings);
                break;
            case "AboutFragment":
                navigationView.setCheckedItem(R.id.nav_about);
                break;

        }

        setDrawerIndicator();
    }

    private void setDrawerIndicator() {
        boolean rootFragment = navigationManager.isRootFragmentVisible();

        toggle.setDrawerIndicatorEnabled(rootFragment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(!rootFragment);

        if (rootFragment) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawer.openDrawer(GravityCompat.START);
                }
            });
        }
        else {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        toggle.syncState();
    }


}
