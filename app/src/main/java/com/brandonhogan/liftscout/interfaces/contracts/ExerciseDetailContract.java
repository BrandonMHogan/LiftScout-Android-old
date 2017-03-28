package com.brandonhogan.liftscout.interfaces.contracts;

import com.brandonhogan.liftscout.repository.model.Exercise;

/**
 * Created by Brandon on 3/27/2017.
 * Description :
 */

public interface ExerciseDetailContract {
    interface View {
        void setTitle(String name);
        void setupControlValues(Exercise exercise);
        void onSaveSuccess();
    }

    interface Presenter {
        void viewCreated();
        double getDefaultIncrement();
        void onSave(String name, int increment, int restTimer, boolean isAuto, boolean isSound, boolean isVibrate);
    }
}
