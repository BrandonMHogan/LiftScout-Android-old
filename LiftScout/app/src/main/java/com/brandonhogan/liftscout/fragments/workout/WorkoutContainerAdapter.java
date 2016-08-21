package com.brandonhogan.liftscout.fragments.workout;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.brandonhogan.liftscout.fragments.workout.tracker.WorkoutTrackFragment;


public class WorkoutContainerAdapter extends FragmentStatePagerAdapter {
    int exerciseId;

    public WorkoutContainerAdapter(FragmentManager fm, int exerciseId) {
        super(fm);
        this.exerciseId = exerciseId;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return WorkoutTrackFragment.newInstance(exerciseId);
            case 1:
                return WorkoutHistoryFragment.newInstance(exerciseId);
            case 2:
                return WorkoutHistoryFragment.newInstance(exerciseId);
            case 3:
                return WorkoutHistoryFragment.newInstance(exerciseId);
            case 4:
                return WorkoutHistoryFragment.newInstance(exerciseId);
            case 5:
                return WorkoutHistoryFragment.newInstance(exerciseId);

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 6;
    }
}