package com.brandonhogan.liftscout.views.graphs.exercises;

import com.brandonhogan.liftscout.core.model.CategoryGraph;

import java.util.ArrayList;

/**
 * Created by Brandon on 3/1/2017.
 * Description :
 */

public class GraphExercisesContract {
    interface View {
        void setSelectedExercise(int id, String name);
    }

    interface Presenter {
        void viewCreated();
        void onResume();
    }
}
