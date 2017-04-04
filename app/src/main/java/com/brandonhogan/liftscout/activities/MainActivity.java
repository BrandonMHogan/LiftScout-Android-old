package com.brandonhogan.liftscout.activities;

import android.animation.ObjectAnimator;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.events.SearchViewEvent;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.managers.NavigationManager;
import com.brandonhogan.liftscout.managers.NotificationServiceManager;
import com.brandonhogan.liftscout.managers.ProgressManager;
import com.brandonhogan.liftscout.repository.DatabaseRealm;
import com.brandonhogan.liftscout.utils.DateUtil;
import com.brandonhogan.liftscout.utils.LogUtil;
import com.brandonhogan.liftscout.utils.constants.DefaultScreens;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements NavigationManager.NavigationListener {


    // Private Static Properties
    //
    private static final String SAVE_STATE_TODAY_PROGRESS_DATE = "saveStateTodayProgressDate";


    // Bindings
    //
    @Bind(R.id.bottom_navigation)
    BottomNavigationViewEx bottomNav;

    @Bind(R.id.toolbar)
    Toolbar toolbar;


    // Injections
    //
    @Inject
    ProgressManager progressManager;

    @Inject
    NavigationManager navigationManager;

    @Inject
    DatabaseRealm databaseRealm;

    // Private Properties
    //

    private SearchView searchView;
    private MenuItem settingsItem, aboutItem;
    private boolean isBottomNavVisable = true;


    // Overrides
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        Injector.getAppComponent().inject(this);

        setSupportActionBar(toolbar);
        handleIntent(getIntent());

        if (findViewById(R.id.fragment_manager) != null) {

            navigationManager.init(getFragmentManager());
            navigationManager.setNavigationListener(this);

            setupBottomNavigation();

            //This is set when restoring from a previous state,
            //so we do not want to try and load a new fragment
            if (savedInstanceState != null) {
                Date todayDate = (Date)savedInstanceState.getSerializable(SAVE_STATE_TODAY_PROGRESS_DATE);
                progressManager.setTodayProgress(todayDate);
                return;
            }

            // If no notifications or any other kind of bundle was set, we will default to calendar or today
            if (userManager.getHomeDefaultValue().equals(DefaultScreens.CALENDAR))
                navigationManager.startCalendar();
            else
                navigationManager.startToday(false);


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
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }
    }

    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        settingsItem = menu.findItem(R.id.action_main_settings);
        aboutItem = menu.findItem(R.id.action_about);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));



        searchView.setOnSearchClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Search view is expanded
                searchOpened();
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose()
            {
                //Search View is collapsed
                searchClosed();
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                // use this method when query submitted
                //Log.d(getTAG(), "onQueryTextSubmit: " + query);
                EventBus.getDefault().post(new SearchViewEvent(true, query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                // use this method for auto complete search process
                //Log.d(getTAG(), "onQueryTextChange: " + newText);
                EventBus.getDefault().post(new SearchViewEvent(true, newText));
                return false;
            }
        });

        return true;
    }

    public void searchClosed() {
        showBottomNav();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        EventBus.getDefault().post(new SearchViewEvent(false, null));

        if (settingsItem != null)
            settingsItem.setVisible(true);
        if (aboutItem != null)
            aboutItem.setVisible(true);
    }

    public void searchOpened() {
        hideBottomNav();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EventBus.getDefault().post(new SearchViewEvent(true, null));

        if (settingsItem != null)
            settingsItem.setVisible(false);
        if (aboutItem != null)
            aboutItem.setVisible(false);
    }

    public void showBottomNav() {
        if (bottomNav != null && !isBottomNavVisable) {
            bottomNav.setVisibility(View.VISIBLE);
            ObjectAnimator animator = ObjectAnimator.ofFloat(bottomNav, "translationY", 0);
            animator.setDuration(400);
            animator.start();
            isBottomNavVisable = !isBottomNavVisable;
        }
    }

    public void hideBottomNav() {
        if (bottomNav != null && isBottomNavVisable) {
            bottomNav.setVisibility(View.GONE);
            ObjectAnimator animator = ObjectAnimator.ofFloat(bottomNav, "translationY", 250.0f);
            animator.setDuration(650);
            animator.start();
            isBottomNavVisable = !isBottomNavVisable;
        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_main_settings).setVisible(true);
        menu.findItem(R.id.action_about).setVisible(true);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserData();
        navigationManager.setNavigationListener(this);
        navigationManager.setmFragmentManager(getFragmentManager());
        navigationManager.setBottomNav(bottomNav);
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateUserData();
        navigationManager.setNavigationListener(null); // Prevent memory leak on recreate
        navigationManager.setmFragmentManager(null);
        navigationManager.setBottomNav(null);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               if (!searchView.isIconified()) {
                   searchView.setIconified(true);
                   searchView.setIconified(true);
               }

                break;
            case R.id.action_main_settings:
                navigationManager.startSettings();
                break;
            case R.id.action_about:
                navigationManager.startAbout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        }
        else {
            if (getFragmentManager().getBackStackEntryCount() == 1) {
                // we have only one fragment left so we would close the application with this back
                endActivity();

            } else {
                navigationManager.navigateBack(this);
            }
        }
    }

    // Public Functions
    //
    public void setTitle(String title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    public SearchView getSearchView() {
        return searchView;
    }

    // Private Functions
    //

    private void setupBottomNavigation() {
        bottomNav.enableAnimation(false);
        bottomNav.enableShiftingMode(false);
        bottomNav.enableItemShiftingMode(false);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_today:
                        if (!progressManager.getCurrentDate().equals(DateUtil.trimTimeFromDate(new Date())))
                            navigationManager.startToday(true);
                        else
                            navigationManager.startToday(false);
                        return true;
                    case R.id.nav_calendar:
                        navigationManager.startCalendar();
                        return true;
                    case R.id.nav_exercises:
                        navigationManager.startExerciseContainer();
                        return true;
                    case R.id.nav_graphs:
                        navigationManager.startGraphsContainer(false);
                        return true;
                }

                return false;
            }
        });

        navigationManager.setBottomNav(bottomNav);
    }

    @Override
    public void onBackstackChanged() {

//        Fragment f = getFragmentManager().findFragmentById(R.id.fragment_manager);
//
//        if (f instanceof SettingsListFragment) {
//            setBottomNavSelectedPos(0);
//        }
//        else if (f instanceof CategoryListFragment) {
//            setBottomNavSelectedPos(1);
//        }
//        else if (f instanceof AnalyticsContainerFragment) {
//            setBottomNavSelectedPos(2);
//        }
//        else if (f instanceof CalendarFragment) {
//            setBottomNavSelectedPos(3);
//        }
//        else if (f instanceof TodayContainerFragment) {
//            setBottomNavSelectedPos(4);
//        }
    }

    private void updateUserData() {
        userManager.lastUsed(new Date());
    }

    private void endActivity() {
        finish();
    }
}
