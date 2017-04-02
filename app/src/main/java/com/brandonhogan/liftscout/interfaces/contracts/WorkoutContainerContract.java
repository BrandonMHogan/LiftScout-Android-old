package com.brandonhogan.liftscout.interfaces.contracts;

import java.util.ArrayList;

/**
 * Created by Brandon on 3/6/2017.
 * Description :
 */

public interface WorkoutContainerContract {
    interface View {
        void onRestTimerTick(int time);
        void onRestTimerTerminate(boolean vibrate, boolean sound);
        void deleteSetSuccess();
        void favouriteUpdated();
    }

    interface Presenter {
        void viewCreated();
        void onDestroyView();
        int getExerciseId();
        int getCategoryId();
        String getExerciseName();
        long getDateLong();
        void onSettingsSave(int timerValue, boolean vibrate, boolean sound, boolean autoStart, int autoIncrementIndex);
        int getExerciseRestTimer();
        int getRemainingRestTime();
        boolean getExerciseRestVibrate();
        boolean getExerciseRestSound();
        boolean getExerciseRestAutoStart();
        int getExerciseIncrementIndex();
        ArrayList<Double> getExerciseIncrementList();

        void onTimerClicked();
        void restTimerNotification(int time);
        void onRestTimerStop();
        void onDeleteSet();
        void onFavClicked();
        boolean isFavourite();
    }
}
