package com.brandonhogan.liftscout.views.workout.graph;

/**
 * Created by Brandon on 2/15/2017.
 * Description :
 */

public interface GraphContract {

    interface View {
        void setupGraph(int exerciseId, String name);
    }

    interface Presenter {
        void viewCreated();
    }
}
