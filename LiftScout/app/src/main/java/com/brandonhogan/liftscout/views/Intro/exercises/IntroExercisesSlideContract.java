package com.brandonhogan.liftscout.views.Intro.exercises;

import java.util.ArrayList;

/**
 * Created by Brandon on 2/27/2017.
 * Description :
 */

public class IntroExercisesSlideContract {
    interface View {
        void exercisesCreated(String value);
        String getStringValue(int value);
        int getColor(int color);
    }

    interface Presenter {
        void viewCreated();
        void onButtonPressed();
    }
}
