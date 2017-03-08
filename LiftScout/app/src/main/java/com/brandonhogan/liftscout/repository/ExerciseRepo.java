package com.brandonhogan.liftscout.repository;

import com.brandonhogan.liftscout.core.model.Exercise;

import io.realm.RealmResults;

public interface ExerciseRepo {

    RealmResults<Exercise> getExercises(int categoryId);
    Exercise getExercise(int exerciseId);
    void setExercise(Exercise exercise);
    void deleteExercise(int exercise);
    void setExerciseRestTimer(int exerciseId, int time);
    int getExerciseRestTimer(int exerciseId);
    void setExerciseRestVibrate(int exerciseId, boolean vibrate);
    void setExerciseRestSound(int exerciseId, boolean sound);
    void setExerciseRestAutoStart(int exerciseId, boolean autoStart);
    boolean getExerciseRestVibrate(int exerciseId);
    boolean getExerciseRestSound(int exerciseId);
    boolean getExerciseRestAutoStart(int exerciseId);
}
