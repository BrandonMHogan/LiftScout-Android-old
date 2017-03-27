package com.brandonhogan.liftscout.interfaces.contracts;

/**
 * Created by Brandon on 3/27/2017.
 * Description :
 */

public interface ExerciseDetailContract {
    interface View {
        void setTitle(String name);
    }

    interface Presenter {
        void viewCreated();
    }
}
