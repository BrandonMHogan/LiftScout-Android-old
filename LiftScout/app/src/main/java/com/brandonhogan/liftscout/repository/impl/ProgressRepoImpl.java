package com.brandonhogan.liftscout.repository.impl;

import android.util.Log;

import com.brandonhogan.liftscout.core.model.Progress;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.DatabaseRealm;
import com.brandonhogan.liftscout.repository.ProgressRepo;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

public class ProgressRepoImpl implements ProgressRepo {

    private static final String TAG = "ProgressRepoImpl";

    @Inject
    DatabaseRealm databaseRealm;

    public ProgressRepoImpl() {
        Injector.getAppComponent().inject(this);
    }


    @Override
    public Progress getProgress(long progressId) {
        return databaseRealm.getRealmInstance()
                .where(Progress.class)
                .equalTo(Progress.ID, progressId)
                .findFirst();
    }

    @Override
    public Progress getProgress(Date date) {
        return databaseRealm.getRealmInstance()
                .where(Progress.class)
                .equalTo(Progress.DATE, date)
                .findFirst();
    }

    @Override
    public RealmResults<Progress> getAllProgressForMonth(int month, int year) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);

        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date first = cal.getTime();

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date last = cal.getTime();

        return databaseRealm.getRealmInstance()
                .where(Progress.class)
                .between(Progress.DATE, first, last)
                .findAll();
    }

    @Override
    public Progress setProgress(Progress progress) {

        try {
            databaseRealm.getRealmInstance().beginTransaction();
            databaseRealm.getRealmInstance().copyToRealmOrUpdate(progress);
            databaseRealm.getRealmInstance().commitTransaction();
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            databaseRealm.getRealmInstance().cancelTransaction();
            return null;
        }

        return  progress;
    }
}
