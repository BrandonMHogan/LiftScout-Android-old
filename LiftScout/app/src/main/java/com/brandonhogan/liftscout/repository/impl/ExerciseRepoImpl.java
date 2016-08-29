package com.brandonhogan.liftscout.repository.impl;

import android.util.Log;

import com.brandonhogan.liftscout.core.model.Exercise;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.DatabaseRealm;
import com.brandonhogan.liftscout.repository.ExerciseRepo;

import javax.inject.Inject;

import io.realm.RealmList;
import io.realm.RealmResults;

public class ExerciseRepoImpl implements ExerciseRepo {

    private static final String TAG = "ExerciseRepoImpl";

    @Inject
    DatabaseRealm databaseRealm;

    public ExerciseRepoImpl() {
        Injector.getAppComponent().inject(this);
    }

    private int getNextKey() {
        Number max = databaseRealm.getRealmInstance().where(Exercise.class).max(Exercise.ID);
        return (max != null) ? max.intValue() + 1 : 0;
    }

    @Override
    public RealmResults<Exercise> getExercises(int categoryId) {
        return databaseRealm.getRealmInstance()
            .where(Exercise.class)
            .equalTo(Exercise.CATEGORY_ID, categoryId)
            .findAll();
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
            if (exercise.getId() == 0)
                exercise.setId(getNextKey());

            databaseRealm.getRealmInstance().beginTransaction();
            databaseRealm.getRealmInstance().copyToRealmOrUpdate(exercise);
            databaseRealm.getRealmInstance().commitTransaction();
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    @Override
    public void deleteExercise(int exerciseId) {
        try {
            databaseRealm.getRealmInstance().beginTransaction();

            databaseRealm.getRealmInstance()
                    .where(Exercise.class)
                    .equalTo(Exercise.ID, exerciseId)
                    .findFirst()
                    .deleteFromRealm();

            databaseRealm.getRealmInstance().commitTransaction();
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }
}
