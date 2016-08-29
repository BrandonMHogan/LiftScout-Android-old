package com.brandonhogan.liftscout.repository;

import com.brandonhogan.liftscout.core.model.Exercise;

public interface ExerciseRepo {

    Exercise getExercise(int exerciseId);
    void setExercise(Exercise exercise);

}
