package com.brandonhogan.liftscout.fragments.workout;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.brandonhogan.liftscout.fragments.workout.tracker.WorkoutTrackFragment;


public class WorkoutContainerAdapter extends FragmentStatePagerAdapter {
    long progressId;
    int exerciseId;

    public WorkoutContainerAdapter(FragmentManager fm, long progressId, int exerciseId) {
        super(fm);
        this.progressId = progressId;
        this.exerciseId = exerciseId;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return WorkoutTrackFragment.newInstance(progressId, exerciseId);
            case 1:
                return WorkoutHistoryFragment.newInstance(progressId);
            case 2:
                return WorkoutHistoryFragment.newInstance(progressId);
            case 3:
                return WorkoutHistoryFragment.newInstance(progressId);
            case 4:
                return WorkoutHistoryFragment.newInstance(progressId);
            case 5:
                return WorkoutHistoryFragment.newInstance(progressId);

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 6;
    }
}