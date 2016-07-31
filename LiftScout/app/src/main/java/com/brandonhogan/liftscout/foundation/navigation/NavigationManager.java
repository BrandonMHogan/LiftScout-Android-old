package com.brandonhogan.liftscout.foundation.navigation;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.fragments.ExerciseTypeListFragment;
import com.brandonhogan.liftscout.fragments.home.HomeFragment;
import com.brandonhogan.liftscout.fragments.base.BaseFragment;
import com.brandonhogan.liftscout.fragments.calendar.CalendarFragment;
import com.brandonhogan.liftscout.fragments.settings.SettingsListFragment;
import com.brandonhogan.liftscout.fragments.settings.SettingsProfileFragment;

public class NavigationManager {

    // Listener
    //
    public interface NavigationListener {
        void onBackstackChanged();
    }


    // Private Properties
    //
    private FragmentManager mFragmentManager;
    private NavigationListener navigationListener;
    private boolean isInTransition = false;

    // Public Properties
    //
    public void setInTransition(boolean inTransition) {
        isInTransition = inTransition;
    }

    public Fragment getCurrentFragment() {
        return mFragmentManager.findFragmentById(R.id.fragment_manager);
    }

    /**
     * Initialize the NavigationManager with a FragmentManager, which will be used at the
     * fragment transactions.
     *
     * @param fragmentManager
     */
    public void init(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
        mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (navigationListener != null) {
                    navigationListener.onBackstackChanged();
                }
            }
        });
    }

    /**
     * Displays the next fragment
     *
     * @param fragment
     */
    private boolean open(BaseFragment fragment) {
        if (mFragmentManager == null)
            return false;

        if(fragment == null)
            return false;

        // If a fragment is already in transition, prevent additional replacement
        if (isInTransition)
            return false;

        Fragment currentFragment = mFragmentManager.findFragmentById(R.id.fragment_manager);

        // Do not reload the same fragment current in the fragment container
        if (currentFragment != null && fragment.getClass().getName().equals(currentFragment.getClass().getName()))
            return false;

        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_from_right,
                        R.anim.slide_to_left,
                        R.anim.slide_from_left,
                        R.anim.slide_to_right)
                .replace(R.id.fragment_manager, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();

        return true;
    }

    /**
     * pops every fragment and starts the given fragment as a new one.
     *
     * @param fragment
     */
    private void openAsRoot(BaseFragment fragment) {
        popToHomeFragment();
        open(fragment);
    }

    private void openAsHome(BaseFragment fragment) {
        popEveryFragment();
        open(fragment);
    }


    /**
     * Pops all the queued fragments
     */
    private void popEveryFragment() {
        // Clear all back stack.
        int backStackCount = mFragmentManager.getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {

            // Get the back stack fragment id.
            int backStackId = mFragmentManager.getBackStackEntryAt(i).getId();

            mFragmentManager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }
    }

    // Will pop all but the first fragment in the back stack which *should* be home
    private void popToHomeFragment() {
        int backStackCount = mFragmentManager.getBackStackEntryCount();
        for (int i = 1; i < backStackCount; i++) {

            // Get the back stack fragment id.
            int backStackId = mFragmentManager.getBackStackEntryAt(i).getId();

            mFragmentManager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }
    }

    /**
     * Navigates back by popping teh back stack. If there is no more items left we finish the current activity.
     *
     * @param baseActivity
     */
    public void navigateBack(Activity baseActivity) {
        if (mFragmentManager.getBackStackEntryCount() == 0) {
            // we can finish the base activity since we have no other fragments
            baseActivity.finish();
        } else {
            mFragmentManager.popBackStackImmediate();
        }
    }

    public void startHome() {
        BaseFragment fragment = HomeFragment.newInstance();
        openAsHome(fragment);
    }

    public void startCalendar() {
        BaseFragment fragment = CalendarFragment.newInstance();
        openAsRoot(fragment);
    }


    // Settings
    //
    public void startSettings() {
        BaseFragment fragment = SettingsListFragment.newInstance();
        openAsRoot(fragment);
    }

    public void startSettingsProfile() {
        BaseFragment fragment = SettingsProfileFragment.newInstance();
        open(fragment);
    }


    public void startExerciseTypeList() {
        BaseFragment fragment = ExerciseTypeListFragment.newInstance();
        openAsRoot(fragment);
    }


    /**
     * @return true if the current fragment displayed is a root fragment
     */
    public boolean isRootFragmentVisible() {
        return mFragmentManager.getBackStackEntryCount() <= 1;
    }

    public NavigationListener getNavigationListener() {
        return navigationListener;
    }

    public void setNavigationListener(NavigationListener navigationListener) {
        this.navigationListener = navigationListener;
    }
}
