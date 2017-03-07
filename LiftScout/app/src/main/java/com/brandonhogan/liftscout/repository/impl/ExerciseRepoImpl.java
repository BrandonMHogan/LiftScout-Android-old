package com.brandonhogan.liftscout.repository.impl;

import android.util.Log;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.model.Exercise;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.DatabaseRealm;
import com.brandonhogan.liftscout.repository.ExerciseRepo;

import javax.inject.Inject;

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
        return (max != null) ? max.intValue() + 1 : 1;
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
            databaseRealm.getRealmInstance().cancelTransaction();
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
            databaseRealm.getRealmInstance().cancelTransaction();
        }
    }

    @Override
    public int getExerciseRestTimer(int exerciseId) {
        Exercise exercise = databaseRealm.getRealmInstance().where(Exercise.class).equalTo(Exercise.ID, exerciseId).findFirst();

        if (exercise!= null && exercise.isValid())
            return exercise.getRestTimer();
        else
            return R.integer.default_rest_timer;
    }

    @Override
    public void setExerciseRestTimer(int exerciseId, int time) {
        try {
            databaseRealm.getRealmInstance().beginTransaction();

            Exercise exercise = databaseRealm.getRealmInstance().where(Exercise.class).equalTo(Exercise.ID, exerciseId).findFirst();
            if (exercise != null && exercise.isValid()) {
                exercise.setRestTimer(time);
                databaseRealm.getRealmInstance().commitTransaction();
            }
            else {
                databaseRealm.getRealmInstance().cancelTransaction();
            }
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            databaseRealm.getRealmInstance().cancelTransaction();
        }
    }

    @Override
    public boolean getExerciseRestVibrate(int exerciseId) {
        Exercise exercise = databaseRealm.getRealmInstance().where(Exercise.class).equalTo(Exercise.ID, exerciseId).findFirst();

        return exercise!= null && exercise.isValid() && exercise.isRestVibrate();
    }

    @Override
    public void setExerciseRestVibrate(int exerciseId, boolean vibrate) {
        try {
            databaseRealm.getRealmInstance().beginTransaction();

            Exercise exercise = databaseRealm.getRealmInstance().where(Exercise.class).equalTo(Exercise.ID, exerciseId).findFirst();
            if (exercise != null && exercise.isValid()) {
                exercise.setRestVibrate(vibrate);
                databaseRealm.getRealmInstance().commitTransaction();
            }
            else {
                databaseRealm.getRealmInstance().cancelTransaction();
            }
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            databaseRealm.getRealmInstance().cancelTransaction();
        }
    }

    @Override
    public boolean getExerciseRestSound(int exerciseId) {
        Exercise exercise = databaseRealm.getRealmInstance().where(Exercise.class).equalTo(Exercise.ID, exerciseId).findFirst();

        return exercise!= null && exercise.isValid() && exercise.isRestSound();
    }

    @Override
    public void setExerciseRestSound(int exerciseId, boolean sound) {
        try {
            databaseRealm.getRealmInstance().beginTransaction();

            Exercise exercise = databaseRealm.getRealmInstance().where(Exercise.class).equalTo(Exercise.ID, exerciseId).findFirst();
            if (exercise != null && exercise.isValid()) {
                exercise.setRestSound(sound);
                databaseRealm.getRealmInstance().commitTransaction();
            }
            else {
                databaseRealm.getRealmInstance().cancelTransaction();
            }
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            databaseRealm.getRealmInstance().cancelTransaction();
        }
    }
}
