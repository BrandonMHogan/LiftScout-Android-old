package com.brandonhogan.liftscout.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.foundation.model.User;
import com.brandonhogan.liftscout.fragments.HomeFragment;
import com.brandonhogan.liftscout.fragments.base.BaseFragment;
import com.brandonhogan.liftscout.fragments.base.FragmentListener;
import com.brandonhogan.liftscout.fragments.calendar.CalendarFragment;

import java.util.Date;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FragmentManager.OnBackStackChangedListener,
        FragmentListener {

    // Private Properties
    //
    private boolean isInTransition = false;
    private BaseFragment currentFragment;
    private DrawerLayout drawer;


    // Overrides
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.fragment_manager) != null) {
            //This is set when restoring from a previous state,
            //so we do not want to try and load a new fragment
            if (savedInstanceState != null) {
                return;
            }

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            replaceFragment(new HomeFragment());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateUserData();
    }

    @Override
    public void onBackStackChanged() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            replaceFragment(new HomeFragment());
        } else if (id == R.id.nav_calendar) {
            replaceFragment(new CalendarFragment());
        } else if (id == R.id.nav_exercises) {

        } else if (id == R.id.nav_routines) {

        } else if (id == R.id.nav_graphs) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_about) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    // Public Functions
    //
    public void setTitle(String title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }


    // Private Functions
    //
    private void updateUserData() {
        getRealm().beginTransaction();
        User user = getRealm().where(User.class).findFirst();
        user.setLastUsed(new Date());
        getRealm().commitTransaction();
    }

    private boolean replaceFragment(BaseFragment fragment) {
        return replaceFragment(fragment, 0, 0);
    }

    private boolean replaceFragment(BaseFragment fragment, int animIn, int animOut) {
        return fragmentReplaceTransaction(fragment, animIn, animOut);
    }

    private boolean fragmentReplaceTransaction(BaseFragment newFragment, int animIn, int animOut) {

        if(newFragment == null)
            return false;

        // If a fragment is already in transition, prevent additional replacement
        if (isInTransition)
            return false;

        // Do not reload the same fragment current in the fragment container
        if (currentFragment != null && newFragment.getClass().getName().equals(currentFragment.getClass().getName()))
            return false;

        currentFragment = newFragment;
        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(currentFragment.getClassTag(), 0);

        //fragment not in back stack, create it.
        if (!fragmentPopped){

            // If animation not set, use the defaults
            if (animIn == 0) {
                animIn = R.anim.slide_from_right;
            }
            if (animOut == 0) {
                animOut = R.anim.slide_to_left;
            }

            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(animIn, animOut, R.anim.slide_from_left, R.anim.slide_to_right)
                    .replace(R.id.fragment_manager, newFragment)
                    .addToBackStack(currentFragment.getClassTag())
                    .commit();
        }

        return true;
    }

    // Fragment Callbacks
    //
    @Override
    public void fragmentTransitionTo(BaseFragment fragment) {
        replaceFragment(fragment);
    }

    @Override
    public void fragmentTransitionTo(BaseFragment fragment, int animIn, int animOut) {
        replaceFragment(fragment, animIn, animOut);
    }

    @Override
    public void fragmentTransitionStarted() {
        isInTransition = true;
    }

    @Override
    public void fragmentTransitionEnded() {
        isInTransition = false;
    }
}
