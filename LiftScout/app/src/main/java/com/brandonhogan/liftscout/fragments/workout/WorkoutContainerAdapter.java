package com.brandonhogan.liftscout.fragments.workout;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;


public class WorkoutContainerAdapter extends FragmentStatePagerAdapter {
    int setId;

    public WorkoutContainerAdapter(FragmentManager fm, int setId) {
        super(fm);
        this.setId = setId;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return WorkoutTrackFragment.newInstance(setId);
            case 1:
                return WorkoutHistoryFragment.newInstance(setId);
            case 2:
                return WorkoutHistoryFragment.newInstance(setId);
            case 3:
                return WorkoutHistoryFragment.newInstance(setId);
            case 4:
                return WorkoutHistoryFragment.newInstance(setId);
            case 5:
                return WorkoutHistoryFragment.newInstance(setId);

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 6;
    }
}