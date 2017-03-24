package com.brandonhogan.liftscout.interfaces.contracts;

/**
 * Created by Brandon on 2/27/2017.
 * Description :
 */

public interface IntroExerciseContract {
    interface View {
        void exercisesCreated(boolean isSet, String value);
        String getStringValue(int value);
        int getColor(int color);
    }

    interface Presenter {
        void viewCreated();
        void onButtonPressed();
    }
}
