package com.brandonhogan.liftscout.repository.impl;

import android.util.Log;

import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.DatabaseRealm;
import com.brandonhogan.liftscout.repository.ProgressRepo;
import com.brandonhogan.liftscout.repository.model.Progress;
import com.brandonhogan.liftscout.utils.DateUtil;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import io.realm.RealmResults;

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
                .equalTo(Progress.DATE, DateUtil.trimTimeFromDate(date))
                .findFirst();
    }

    @Override
    public RealmResults<Progress> getAllProgressForMonth(int month, int year) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);

        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));

        Date first = DateUtil.trimTimeFromDate(cal.getTime());

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));

        Date last = DateUtil.trimTimeFromDate(cal.getTime());

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
