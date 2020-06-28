package com.brandonhogan.liftscout.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.brandonhogan.liftscout.views.GraphFragment;
import com.brandonhogan.liftscout.views.HistoryFragment;
import com.brandonhogan.liftscout.views.RecordsFragment;
import com.brandonhogan.liftscout.views.WorkoutTrackerFragment;


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
                return WorkoutTrackerFragment.newInstance(exerciseId);
            case 1:
                return HistoryFragment.newInstance(exerciseId);
            case 2:
                return GraphFragment.newInstance(exerciseId);
            case 3:
                return RecordsFragment.newInstance(exerciseId);
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