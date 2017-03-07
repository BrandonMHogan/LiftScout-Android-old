package com.brandonhogan.liftscout.views.workout;

/**
 * Created by Brandon on 3/6/2017.
 * Description :
 */

public interface WorkoutContainerContract {
    interface View {
        void onRestTimerTick(int time);
        void onRestTimerTerminate(boolean vibrate);
        void deleteSetSuccess();
    }

    interface Presenter {
        void viewCreated();
        int getExerciseId();
        String getExerciseName();
        void onSettingsSave(int timerValue, boolean vibrate);
        int getExerciseRestTimer();
        boolean getExerciseRestVibrate();
        void onTimerClicked();
        void onRestTimerStop();
        void onDeleteSet();
    }
}
