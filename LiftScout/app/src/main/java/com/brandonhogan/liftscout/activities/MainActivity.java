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
    private Toolbar toolbar;


    // Overrides
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
            navigationManager.init(getFragmentManager());
            navigationManager.setNavigationListener(this);


            // This needs to be hit first regardless of where a notification will go. Home must
            // be the first item in the back stack
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

        if (id == R.id.nav_home) {
            success = navigationManager.startHome();
        } else if (id == R.id.nav_calendar) {
            success = navigationManager.startCalendar();
        } else if (id == R.id.nav_exercises) {
            success = navigationManager.startExerciseTypeList();
        } else if (id == R.id.nav_routines) {

        } else if (id == R.id.nav_graphs) {

        } else if (id == R.id.nav_settings) {
            success = navigationManager.startSettings();
        } else if (id == R.id.nav_about) {

        }

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
                navigationView.setCheckedItem(R.id.nav_home);
                break;
            case "CalendarFragment":
                navigationView.setCheckedItem(R.id.nav_calendar);
                break;
            case "SettingsListFragment":
                navigationView.setCheckedItem(R.id.nav_settings);
                break;

        }

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
