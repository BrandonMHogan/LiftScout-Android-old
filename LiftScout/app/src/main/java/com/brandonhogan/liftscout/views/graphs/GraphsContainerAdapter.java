package com.brandonhogan.liftscout.views.graphs;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.brandonhogan.liftscout.views.graphs.categories.CategoriesFragment;
import com.brandonhogan.liftscout.views.graphs.exercises.ExercisesFragment;
import com.brandonhogan.liftscout.views.workout.graph.GraphFragment;
import com.brandonhogan.liftscout.views.workout.history.HistoryFragment;
import com.brandonhogan.liftscout.views.workout.tracker.TrackerFragment;

/**
 * Created by Brandon on 2/16/2017.
 * Description :
 */

public class GraphsContainerAdapter extends FragmentStatePagerAdapter {

    public GraphsContainerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return CategoriesFragment.newInstance();
            case 1:
                return ExercisesFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}