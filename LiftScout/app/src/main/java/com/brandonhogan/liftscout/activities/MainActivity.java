package com.brandonhogan.liftscout.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.fragments.HomeFragment;
import com.brandonhogan.liftscout.fragments.base.BHFragment;
import com.brandonhogan.liftscout.fragments.base.BHFragmentListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BHFragmentListener {

    private boolean isInTransition = false;
    private FragmentTransaction transaction;
    // Tracks the currently displayed fragment
    private BHFragment currentFragment;

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

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            replaceFragment(new HomeFragment());
        } else if (id == R.id.nav_calendar) {

        } else if (id == R.id.nav_exercises) {

        } else if (id == R.id.nav_routines) {

        } else if (id == R.id.nav_graphs) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean replaceFragment(BHFragment fragment) {
        return replaceFragment(fragment, 0, 0);
    }

    private boolean replaceFragment(BHFragment fragment, int animIn, int animOut) {
        return fragmentReplaceTransaction(fragment, fragment.getBhTAG(), animIn, animOut);
    }

    private boolean fragmentReplaceTransaction(BHFragment newFragment, String tag, int animIn, int animOut) {

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
        boolean fragmentPopped = manager.popBackStackImmediate(tag, 0);

        //fragment not in back stack, create it.
        if (!fragmentPopped){
            transaction = getSupportFragmentManager().beginTransaction();

            // If animation not set, use the defaults
            if (animIn == 0) {
                animIn = R.anim.slide_from_right;
            }
            if (animOut == 0) {
                animOut = R.anim.slide_to_left;
            }

            transaction.setCustomAnimations(animIn, animOut, R.anim.slide_from_left, R.anim.slide_to_right);
            transaction.replace(R.id.fragment_manager, newFragment);
            transaction.addToBackStack(tag);
            transaction.commit();
        }

        return true;
    }

    @Override
    public void fragmentTransitionTo(BHFragment fragment) {
        replaceFragment(fragment);
    }

    @Override
    public void fragmentTransitionTo(BHFragment fragment, int animIn, int animOut) {
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
