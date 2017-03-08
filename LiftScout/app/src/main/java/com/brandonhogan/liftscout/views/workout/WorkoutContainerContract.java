package com.brandonhogan.liftscout.views.workout;

/**
 * Created by Brandon on 3/6/2017.
 * Description :
 */

public interface WorkoutContainerContract {
    interface View {
        void onRestTimerTick(int time);
        void onRestTimerTerminate(boolean vibrate, boolean sound);
        void deleteSetSuccess();
    }

    interface Presenter {
        void viewCreated();
        void onDestroyView();
        int getExerciseId();
        String getExerciseName();
        long getDateLong();
        void onSettingsSave(int timerValue, boolean vibrate, boolean sound);
        int getExerciseRestTimer();
        int getRemainingRestTime();
        boolean getExerciseRestVibrate();
        boolean getExerciseRestSound();

        void onTimerClicked();
        void restTimerNotification(int time);
        void onRestTimerStop();
        void onDeleteSet();
    }
}
