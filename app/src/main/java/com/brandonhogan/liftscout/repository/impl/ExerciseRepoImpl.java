package com.brandonhogan.liftscout.repository.impl;

import android.util.Log;

import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.DatabaseRealm;
import com.brandonhogan.liftscout.repository.ExerciseRepo;
import com.brandonhogan.liftscout.repository.model.Exercise;
import com.brandonhogan.liftscout.utils.constants.ConstantValues;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
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
    public List<Exercise> getExercises(int categoryId, boolean includeDeleted) {

        if (includeDeleted) {
            return databaseRealm.getRealmInstance()
                    .where(Exercise.class)
                    .equalTo(Exercise.CATEGORY_ID, categoryId)
                    .findAll()
                    .sort(Exercise.NAME);
        }

        return databaseRealm.getRealmInstance()
                .where(Exercise.class)
                .equalTo(Exercise.CATEGORY_ID, categoryId)
                .notEqualTo(Exercise.IS_DELETED, true)
                .findAll()
                .sort(Exercise.NAME);
    }

    @Override
    public Exercise getExercise(int exerciseId) {
        return databaseRealm.getRealmInstance()
                .where(Exercise.class)
                .equalTo(Exercise.ID, exerciseId)
                .findFirst();
    }

    @Override
    public List<Exercise> getAllExercises() {
        return databaseRealm.getRealmInstance()
                .where(Exercise.class)
                .equalTo(Exercise.IS_DELETED, false)
                .findAll()
                .sort(Exercise.NAME);
    }

    @Override
    public List<Exercise> getAllFavouriteExercises() {
        return databaseRealm.getRealmInstance()
                .where(Exercise.class)
                .equalTo(Exercise.IS_DELETED, false)
                .equalTo(Exercise.FAVOURITE, true)
                .findAll()
                .sort(Exercise.NAME);
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
    public void updateExercise(int id, String name, boolean isFav, double increment, boolean vibrate, boolean sound, boolean autoStart, int restTimer) {
        try {

            databaseRealm.getRealmInstance().beginTransaction();

            Exercise exercise = databaseRealm.getRealmInstance().where(Exercise.class).equalTo(Exercise.ID, id).findFirst();

            exercise.setName(name);
            exercise.setFavourite(isFav);
            exercise.setIncrement(increment);
            exercise.setRestVibrate(vibrate);
            exercise.setRestSound(sound);
            exercise.setRestAutoStart(autoStart);
            exercise.setRestTimer(restTimer);

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

            Exercise exercise = databaseRealm.getRealmInstance()
                    .where(Exercise.class)
                    .equalTo(Exercise.ID, exerciseId)
                    .findFirst();

            exercise.setDeleted(true);
            exercise.setDeleteDate(new Date());

            databaseRealm.getRealmInstance().commitTransaction();
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            databaseRealm.getRealmInstance().cancelTransaction();
        }
    }

    @Override
    public void deleteAllExercisesForCategory(int categoryId) {
        try {
            databaseRealm.getRealmInstance().beginTransaction();

                    RealmResults<Exercise> exercises = databaseRealm.getRealmInstance()
                            .where(Exercise.class)
                            .equalTo(Exercise.CATEGORY_ID, categoryId)
                            .findAll();

            for(Exercise exercise :exercises) {
                exercise.setDeleted(true);
                exercise.setDeleteDate(new Date());
            }

            databaseRealm.getRealmInstance().commitTransaction();
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            databaseRealm.getRealmInstance().cancelTransaction();
        }
    }

    @Override
    public Observable<Boolean> deleteAllExercises() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                try {
                    databaseRealm.getRealmInstance().beginTransaction();

                    databaseRealm.getRealmInstance()
                            .where(Exercise.class)
                            .findAll().deleteAllFromRealm();

                    databaseRealm.getRealmInstance().commitTransaction();

                    e.onNext(true);
                    e.onComplete();
                }
                catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                    databaseRealm.getRealmInstance().cancelTransaction();
                    e.onError(ex);
                }
            }
        });
    }

    @Override
    public int getExerciseRestTimer(int exerciseId) {
        Exercise exercise = databaseRealm.getRealmInstance().where(Exercise.class).equalTo(Exercise.ID, exerciseId).findFirst();

        if (exercise!= null && exercise.isValid())
            return exercise.getRestTimer();
        else
            return ConstantValues.REST_TIME_DEFAULT;
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

    @Override
    public boolean getExerciseRestAutoStart(int exerciseId) {
        Exercise exercise = databaseRealm.getRealmInstance().where(Exercise.class).equalTo(Exercise.ID, exerciseId).findFirst();
        return exercise!= null && exercise.isValid() && exercise.isRestAutoStart();
    }

    @Override
    public void setExerciseRestAutoStart(int exerciseId, boolean autoStart) {
        try {
            databaseRealm.getRealmInstance().beginTransaction();

            Exercise exercise = databaseRealm.getRealmInstance().where(Exercise.class).equalTo(Exercise.ID, exerciseId).findFirst();
            if (exercise != null && exercise.isValid()) {
                exercise.setRestAutoStart(autoStart);
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
    public double getExerciseIncrement(int exerciseId) {
        Exercise exercise = databaseRealm.getRealmInstance().where(Exercise.class).equalTo(Exercise.ID, exerciseId).findFirst();
        if (exercise!= null && exercise.isValid())
            return exercise.getIncrement();
        else
            return 0;
    }

    @Override
    public void setExerciseIncrement(int exerciseId, double increment) {
        try {
            databaseRealm.getRealmInstance().beginTransaction();

            Exercise exercise = databaseRealm.getRealmInstance().where(Exercise.class).equalTo(Exercise.ID, exerciseId).findFirst();
            if (exercise != null && exercise.isValid()) {
                exercise.setIncrement(increment);
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
