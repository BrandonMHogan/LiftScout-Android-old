package com.brandonhogan.liftscout.repository.impl;

import android.util.Log;

import com.brandonhogan.liftscout.core.model.Progress;
import com.brandonhogan.liftscout.core.model.User;
import com.brandonhogan.liftscout.core.model.UserSetting;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.DatabaseRealm;
import com.brandonhogan.liftscout.repository.ProgressRepo;

import java.util.Date;

import javax.inject.Inject;

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
    public Progress setProgress(Progress progress) {

        try {
            databaseRealm.getRealmInstance().beginTransaction();
            databaseRealm.getRealmInstance().copyToRealmOrUpdate(progress);
            databaseRealm.getRealmInstance().commitTransaction();
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            return null;
        }

        return  progress;
    }
}
