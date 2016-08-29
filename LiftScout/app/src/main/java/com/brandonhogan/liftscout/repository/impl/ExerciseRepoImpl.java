package com.brandonhogan.liftscout.repository.impl;

import android.util.Log;

import com.brandonhogan.liftscout.core.model.Exercise;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.DatabaseRealm;
import com.brandonhogan.liftscout.repository.ExerciseRepo;

import javax.inject.Inject;

public class ExerciseRepoImpl implements ExerciseRepo {

    private static final String TAG = "ExerciseRepoImpl";

    @Inject
    DatabaseRealm databaseRealm;

    public ExerciseRepoImpl() {
        Injector.getAppComponent().inject(this);
    }


    @Override
    public Exercise getExercise(int exerciseId) {
        return databaseRealm.getRealmInstance()
                .where(Exercise.class)
                .equalTo(Exercise.ID, exerciseId)
                .findFirst();
    }

    @Override
    public void setExercise(Exercise exercise) {
        try {
            databaseRealm.getRealmInstance().beginTransaction();
            databaseRealm.getRealmInstance().copyToRealmOrUpdate(exercise);
            databaseRealm.getRealmInstance().commitTransaction();
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }
}
