package com.brandonhogan.liftscout.managers;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.features.about.AboutFragment;
import com.brandonhogan.liftscout.interfaces.OnBackPressListener;
import com.brandonhogan.liftscout.views.AnalyticsContainerFragment;
import com.brandonhogan.liftscout.views.CalendarFragment;
import com.brandonhogan.liftscout.views.CategoryDetailFragment;
import com.brandonhogan.liftscout.views.CategoryListFragment;
import com.brandonhogan.liftscout.views.ExerciseDetailFragment;
import com.brandonhogan.liftscout.views.ExerciseListContainerFragment;
import com.brandonhogan.liftscout.views.ExerciseListFragment;
import com.brandonhogan.liftscout.views.SettingsDisplayFragment;
import com.brandonhogan.liftscout.views.SettingsHomeFragment;
import com.brandonhogan.liftscout.views.SettingsListFragment;
import com.brandonhogan.liftscout.views.SettingsProfileFragment;
import com.brandonhogan.liftscout.views.TodayContainerFragment;
import com.brandonhogan.liftscout.views.WorkoutContainerFragment;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class NavigationManager {

    private static final String TAG = "NavigationManager";

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
    private FragmentManager.OnBackStackChangedListener backstackListener;
    private String currentFragmentName;
    private BottomNavigationViewEx bottomNav;
    private boolean bottomNavHidden;


    // Public Properties
    //
    public void setInTransition(boolean inTransition) {
        isInTransition = inTransition;
    }

    public Fragment getCurrentFragment() {
        return mFragmentManager.findFragmentById(R.id.fragment_manager);
    }

    public NavigationManager() {
        backstackListener = new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (navigationListener != null) {
                    navigationListener.onBackstackChanged();
                }
            }
        };
    }

    /**
     * Initialize the NavigationManager with a FragmentManager, which will be used at the
     * fragment transactions.
     *
     * @param fragmentManager
     */
    public void init(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;

        mFragmentManager.removeOnBackStackChangedListener(backstackListener);
        mFragmentManager.addOnBackStackChangedListener(backstackListener);
    }

    /**
     * Displays the next fragment
     *
     */
    private boolean replaceWithTransitions(BaseFragment fragment, int in, int out) {

        currentFragmentName = fragment.getClass().getName();

        mFragmentManager.beginTransaction()
                .setCustomAnimations(in,
                        out,
                        R.animator.fade_in,
                        R.animator.fade_out)
                .replace(R.id.fragment_manager, fragment)
                .addToBackStack(fragment.getTAG())
                .commit();

        return true;
    }

    /**
     * pops every fragment and starts the given fragment as a new one.
     */
    private boolean openWithoutNavBar(BaseFragment fragment) {

        if (!verifyTransition(fragment, false)) {
            Log.d(TAG, "Transition has failed");
            return false;
        }

        hideBottomNav();
        return replaceWithTransitions(fragment, R.animator.fade_in, R.animator.fade_out);
    }

    private boolean openAsRoot(BaseFragment fragment) {
        return openAsRoot(fragment, false);
    }

    private boolean openAsRoot(BaseFragment fragment, boolean force) {
        if (!verifyTransition(fragment, force))
            return false;

        showBottomNav();
        popToHomeFragment();
        return replaceWithTransitions(fragment, R.animator.root_in, R.animator.fade_out);
    }

    private boolean openAsHome(BaseFragment fragment) {
        if (!verifyTransition(fragment, false))
            return false;

        showBottomNav();
        popEveryFragment();
        return replaceWithTransitions(fragment, R.animator.fade_in, R.animator.fade_out);
    }

    private boolean openAsHome(BaseFragment fragment, boolean force) {
        if (!verifyTransition(fragment, force))
            return false;

        showBottomNav();
        popEveryFragment();
        return replaceWithTransitions(fragment, R.animator.fade_in, R.animator.fade_out);
    }

    private boolean open(BaseFragment fragment) {
        if (!verifyTransition(fragment, false)) {
            Log.d(TAG, "Transition has failed");
            return false;
        }

        showBottomNav();
        return replaceWithTransitions(fragment, R.animator.fade_in, R.animator.fade_out);
    }

    private boolean verifyTransition(BaseFragment fragment, boolean force) {
        if (mFragmentManager == null)
            return false;

        if(fragment == null)
            return false;

        Fragment currentFragment = mFragmentManager.findFragmentById(R.id.fragment_manager);

        // Do not reload the same fragment current in the fragment container
        if (!force && currentFragment != null && fragment.getClass().getName().equals(currentFragment.getClass().getName()))
            return false;

        // If a fragment is already in transition, prevent additional replacement
        if (isInTransition)
            return false;

        return true;
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

    private boolean popToFragmentName(String name) {
        return mFragmentManager.popBackStackImmediate(name, 0);
    }

    private boolean popToFragmentPosition(int pos) {
        return mFragmentManager.popBackStackImmediate(pos, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * Navigates back by popping teh back stack. If there is no more items left we finish the current activity.
     */
    public void navigateBack(Activity baseActivity) {
        if (mFragmentManager.getBackStackEntryCount() == 0) {
            // we can finish the base activity since we have no other fragments
            baseActivity.finish();
        } else {

            Fragment currentFragment = mFragmentManager.findFragmentById(R.id.fragment_manager);

            boolean allowPop = true;
            if(currentFragment instanceof OnBackPressListener){
                allowPop = ((OnBackPressListener)currentFragment).onBackPress();
            }

            if(allowPop) {
                showBottomNav();
                mFragmentManager.popBackStack();
            }
        }
    }

    public boolean startToday(boolean force) {
        BaseFragment fragment = TodayContainerFragment.newInstance();
        return openAsHome(fragment, force);
    }

    public boolean startCalendar() {
        BaseFragment fragment = CalendarFragment.newInstance();
        return openAsHome(fragment);
    }


    // Settings
    //
    public boolean startSettings() {
        BaseFragment fragment = SettingsListFragment.newInstance();
        return open(fragment);
    }

    public boolean startSettingsProfile() {
        BaseFragment fragment = SettingsProfileFragment.newInstance();
        return openWithoutNavBar(fragment);
    }

    public boolean startSettingsDisplay() {
        BaseFragment fragment = SettingsDisplayFragment.newInstance();
        return openWithoutNavBar(fragment);
    }

    public boolean startSettingsHome() {
        BaseFragment fragment = SettingsHomeFragment.newInstance();
        return openWithoutNavBar(fragment);
    }


    // About
    //
    public boolean startAbout() {
        BaseFragment fragment = AboutFragment.newInstance();
        return openWithoutNavBar(fragment);
    }

    // Graphs
    //
    public boolean startGraphsContainer(boolean isSearch) {
        BaseFragment fragment = AnalyticsContainerFragment.newInstance();

        if (isSearch)
            return popToFragmentName(fragment.getTAG()) || openAsRoot(fragment);

       return openAsRoot(fragment);
    }

    // Categories / Exercises
    //
    public boolean startExerciseContainer() {
        BaseFragment fragment = ExerciseListContainerFragment.newInstance(false);
        return openAsRoot(fragment);
    }

    public boolean startExerciseContainerAddSet() {
        BaseFragment fragment = ExerciseListContainerFragment.newInstance(true);
        return openAsRoot(fragment);
    }

    public boolean startCategoryListGraphSearch() {
        BaseFragment fragment = CategoryListFragment.newInstance(false);
        return open(fragment);
    }

    public boolean startExerciseList(int categoryId) {
        BaseFragment fragment = ExerciseListFragment.newInstance(categoryId);
        return open(fragment);
    }

    public boolean startExerciseListAddSet(int categoryId) {
        BaseFragment fragment = ExerciseListFragment.newInstance(categoryId, true);
        return open(fragment);
    }

    public boolean startExerciseDetail(int exerciseId, int categoryId) {
        BaseFragment fragment = ExerciseDetailFragment.newInstance(exerciseId, categoryId);
        return openWithoutNavBar(fragment);
    }

    public boolean startExerciseDetail(boolean validCategoryId, int categoryId) {
        BaseFragment fragment = ExerciseDetailFragment.newInstance(validCategoryId, categoryId);
        return openWithoutNavBar(fragment);
    }

    public boolean startCategoryDetail() {
        BaseFragment fragment = CategoryDetailFragment.newInstance();
        return openWithoutNavBar(fragment);
    }

    public boolean startCategoryDetail(int categoryId) {
        BaseFragment fragment = CategoryDetailFragment.newInstance(categoryId);
        return openWithoutNavBar(fragment);
    }

    // Set Edit
    public boolean startWorkoutContainerAsOpen(int exerciseId) {
        BaseFragment fragment = WorkoutContainerFragment.newInstance(exerciseId, 0);
        return open(fragment);
    }

    public boolean startWorkoutContainer(int exerciseId, boolean force) {
        BaseFragment fragment = WorkoutContainerFragment.newInstance(exerciseId, 0);
        return openAsRoot(fragment, force);
    }

    public boolean startWorkoutContainer(int exerciseId, int timer) {
        BaseFragment fragment = WorkoutContainerFragment.newInstance(exerciseId, timer);
        return openAsRoot(fragment);
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

    public FragmentManager getmFragmentManager() {
        return mFragmentManager;
    }

    public void setmFragmentManager(FragmentManager mFragmentManager) {
        this.mFragmentManager = mFragmentManager;
    }

    public String getCurrentFragmentName() {
        return currentFragmentName;
    }


    public void setBottomNav(BottomNavigationViewEx bottomNav) {
        this.bottomNav = bottomNav;
    }

    public void showBottomNav() {
        if (bottomNav != null && bottomNavHidden) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(bottomNav, "translationY", 0);
            animator.setDuration(400);
            animator.start();
            bottomNavHidden = false;
        }
    }

    public void hideBottomNav() {
        if (bottomNav != null && !bottomNavHidden) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(bottomNav, "translationY", 250.0f);
            animator.setDuration(650);
            animator.start();
            bottomNavHidden = true;
        }
    }
}
