package com.brandonhogan.liftscout.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.brandonhogan.liftscout.views.CategoryListFragment;

/**
 * Created by Brandon on 3/29/2017.
 * Description :
 */

public class ExerciseListContainerAdapter extends FragmentStatePagerAdapter {

    boolean isAddSet;

    public ExerciseListContainerAdapter(FragmentManager fm, boolean isAddSet) {
        super(fm);

        this.isAddSet = isAddSet;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return CategoryListFragment.newInstance(isAddSet);
            case 1:
                return CategoryListFragment.newInstance(isAddSet);
            case 2:
                return CategoryListFragment.newInstance(isAddSet);

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}