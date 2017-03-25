package com.brandonhogan.liftscout.interfaces.contracts;

/**
 * Created by Brandon on 3/1/2017.
 * Description :
 */

public interface GraphExercisesContract {
    interface View {
        void setSelectedExercise(int id, String name);
    }

    interface Presenter {
        void viewCreated();
        void onResume();
    }
}
