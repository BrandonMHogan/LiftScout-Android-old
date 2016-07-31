package com.brandonhogan.liftscout.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.foundation.model.User;
import com.brandonhogan.liftscout.foundation.navigation.NavigationManager;

import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        NavigationManager.NavigationListener {


    // Private Properties
    //
    private DrawerLayout drawer;
    private NavigationManager navigationManager;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private SweetAlertDialog dialog;


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
             toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            navigationManager = new NavigationManager();
            navigationManager.init(getSupportFragmentManager());
            navigationManager.setNavigationListener(this);

            navigationManager.startHome();
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
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
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

        if (id == R.id.nav_home) {
            navigationManager.startHome();
        } else if (id == R.id.nav_calendar) {
            navigationManager.startCalendar();
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

    public NavigationManager getNavigationManager() {
        return navigationManager;
    }


    // Private Functions
    //
    private void updateUserData() {
        getRealm().beginTransaction();
        User user = getRealm().where(User.class).findFirst();
        user.setLastUsed(new Date());
        getRealm().commitTransaction();
    }

    private void showExitDialog() {

        dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setConfirmText("Yes,delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        endActivity();
                    }
                })
                .setCancelText("Cancel")
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
                navigationView.setCheckedItem(R.id.nav_home);
                break;
            case "CalendarFragment":
                navigationView.setCheckedItem(R.id.nav_calendar);
                break;

        }

        boolean rootFragment = navigationManager.isRootFragmentVisible();
        //getSupportActionBar().setDisplayShowHomeEnabled(rootFragment);
        //getSupportActionBar().setHomeButtonEnabled(true);
     //   getSupportActionBar().setDisplayHomeAsUpEnabled(rootFragment);
        toggle.setDrawerIndicatorEnabled(rootFragment);
        toggle.syncState();

    }
}
