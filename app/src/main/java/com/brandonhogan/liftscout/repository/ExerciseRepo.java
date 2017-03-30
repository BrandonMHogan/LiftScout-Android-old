package com.brandonhogan.liftscout.repository;

import com.brandonhogan.liftscout.repository.model.Exercise;

import java.util.List;

import io.reactivex.Observable;

public interface ExerciseRepo {

    List<Exercise> getExercises(int categoryId, boolean includeDeleted);
    List<Exercise> getAllExercises();
    List<Exercise> getAllFavouriteExercises();
    Exercise getExercise(int exerciseId);
    void setExercise(Exercise exercise);
    void updateExercise(int id, String name, boolean isFav, double increment, boolean vibrate, boolean sound, boolean autoStart, int restTimer);
    void deleteExercise(int exercise);
    Observable<Boolean> deleteAllExercises();
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
