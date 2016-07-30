package com.brandonhogan.liftscout.foundation.navigation;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.fragments.HomeFragment;
import com.brandonhogan.liftscout.fragments.base.BaseFragment;
import com.brandonhogan.liftscout.fragments.calendar.CalendarFragment;

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
    private BaseFragment currentFragment;


    // Public Properties
    //
    public void setInTransition(boolean inTransition) {
        isInTransition = inTransition;
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
    private boolean open(Fragment fragment) {
            if (mFragmentManager == null)
                return false;

            if(fragment == null)
                return false;

            // If a fragment is already in transition, prevent additional replacement
            if (isInTransition)
                return false;

            // Do not reload the same fragment current in the fragment container
            if (currentFragment != null && fragment.getClass().getName().equals(currentFragment.getClass().getName()))
                return false;

            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_from_right,
                            R.anim.slide_to_left,
                            R.anim.slide_from_left,
                            R.anim.slide_to_right)
                    .replace(R.id.fragment_manager, fragment)
                    .addToBackStack(fragment.toString())
                    .commit();

            return true;
    }

    /**
     * pops every fragment and starts the given fragment as a new one.
     *
     * @param fragment
     */
    private void openAsRoot(Fragment fragment) {
      //  popEveryFragment();
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
        Fragment fragment = HomeFragment.newInstance();
        openAsRoot(fragment);
    }

    public void startCalendar() {
        Fragment fragment = CalendarFragment.newInstance();
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
        navigationListener = navigationListener;
    }
}
