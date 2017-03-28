package com.brandonhogan.liftscout.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.brandonhogan.liftscout.views.GraphExercisesFragment;
import com.brandonhogan.liftscout.views.GraphsCategoriesFragment;

/**
 * Created by Brandon on 2/16/2017.
 * Description :
 */

public class AnalyticsContainerAdapter extends FragmentStatePagerAdapter {

    public AnalyticsContainerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return GraphsCategoriesFragment.newInstance();
            case 1:
                return GraphExercisesFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}