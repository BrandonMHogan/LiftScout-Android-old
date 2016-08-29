package com.brandonhogan.liftscout.repository.impl;

import android.util.Log;

import com.brandonhogan.liftscout.core.model.Progress;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.DatabaseRealm;
import com.brandonhogan.liftscout.repository.SetRepo;

import javax.inject.Inject;

public class SetRepoImpl implements SetRepo {

    private static final String TAG = "SetRepoImpl";

    @Inject
    DatabaseRealm databaseRealm;

    public SetRepoImpl() {
        Injector.getAppComponent().inject(this);
    }

    @Override
    public Set getSet(int setId) {
        return databaseRealm.getRealmInstance()
                .where(Set.class)
                .equalTo(Set.ID, setId)
                .findFirst();
    }

    @Override
    public void addSet(Progress progress, Set set) {
        try {
            databaseRealm.getRealmInstance().beginTransaction();
            progress.getSets().add(set);
            databaseRealm.getRealmInstance().commitTransaction();
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    @Override
    public void updateSet(Set set) {
        try {
            databaseRealm.getRealmInstance().beginTransaction();
            databaseRealm.getRealmInstance().copyToRealmOrUpdate(set);
            databaseRealm.getRealmInstance().commitTransaction();
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    @Override
    public void addRep(Set set, Rep rep) {
        try {
            databaseRealm.getRealmInstance().beginTransaction();
            set.getReps().add(rep);
            databaseRealm.getRealmInstance().commitTransaction();
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    @Override
    public void updateRep(Rep rep) {
        try {
            databaseRealm.getRealmInstance().beginTransaction();
            databaseRealm.getRealmInstance().copyToRealmOrUpdate(rep);
            databaseRealm.getRealmInstance().commitTransaction();
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }
}
