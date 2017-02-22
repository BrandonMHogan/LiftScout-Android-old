package com.brandonhogan.liftscout.views.workout;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.brandonhogan.liftscout.views.workout.graph.GraphFragment;
import com.brandonhogan.liftscout.views.workout.history.HistoryFragment;
import com.brandonhogan.liftscout.views.workout.tracker.TrackerFragment;


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
                return TrackerFragment.newInstance(exerciseId);
            case 1:
                return HistoryFragment.newInstance(exerciseId);
            case 2:
                return GraphFragment.newInstance(exerciseId);
            case 3:
                return HistoryFragment.newInstance(exerciseId);
            case 4:
                return HistoryFragment.newInstance(exerciseId);

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}