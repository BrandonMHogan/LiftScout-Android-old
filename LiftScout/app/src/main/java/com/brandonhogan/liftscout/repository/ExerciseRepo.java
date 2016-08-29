package com.brandonhogan.liftscout.repository;

import com.brandonhogan.liftscout.core.model.Exercise;

import io.realm.RealmResults;

public interface ExerciseRepo {

    RealmResults<Exercise> getExercises(int categoryId);
    Exercise getExercise(int exerciseId);
    void setExercise(Exercise exercise);
    void deleteExercise(int exercise);
}
