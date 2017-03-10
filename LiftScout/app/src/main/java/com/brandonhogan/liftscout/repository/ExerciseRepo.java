package com.brandonhogan.liftscout.repository;

import com.brandonhogan.liftscout.core.model.Exercise;

import io.realm.RealmResults;

public interface ExerciseRepo {

    RealmResults<Exercise> getExercises(int categoryId, boolean includeDeleted);
    Exercise getExercise(int exerciseId);
    void setExercise(Exercise exercise);
    void updateExercise(int id, String name, double increment, boolean vibrate, boolean sound, boolean autoStart, int restTimer);
    void deleteExercise(int exercise);
    void deleteAllExercisesForCategory(int categoryId);
    void setExerciseRestTimer(int exerciseId, int time);
    int getExerciseRestTimer(int exerciseId);
    void setExerciseRestVibrate(int exerciseId, boolean vibrate);
    void setExerciseRestSound(int exerciseId, boolean sound);
    void setExerciseRestAutoStart(int exerciseId, boolean autoStart);
    void setExerciseIncrement(int exerciseId, double increment);
    boolean getExerciseRestVibrate(int exerciseId);
    boolean getExerciseRestSound(int exerciseId);
    boolean getExerciseRestAutoStart(int exerciseId);
    double getExerciseIncrement(int exerciseId);
}
