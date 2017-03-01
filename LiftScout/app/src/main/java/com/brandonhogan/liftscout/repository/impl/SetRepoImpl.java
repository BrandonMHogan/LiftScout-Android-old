package com.brandonhogan.liftscout.repository.impl;

import android.util.Log;

import com.brandonhogan.liftscout.core.model.Category;
import com.brandonhogan.liftscout.core.model.Progress;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.DatabaseRealm;
import com.brandonhogan.liftscout.repository.SetRepo;

import java.util.Date;

import javax.inject.Inject;

import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

public class SetRepoImpl implements SetRepo {

    private static final String TAG = "SetRepoImpl";

    @Inject
    DatabaseRealm databaseRealm;

    public SetRepoImpl() {
        Injector.getAppComponent().inject(this);
    }

    private int getNextSetKey() {
        Number max = databaseRealm.getRealmInstance().where(Set.class).max(Category.ID);
        return (max != null) ? max.intValue() + 1 : 1;
    }

    private int getNextRepKey() {
        Number max = databaseRealm.getRealmInstance().where(Rep.class).max(Category.ID);
        return (max != null) ? max.intValue() + 1 : 1;
    }

    @Override
    public RealmResults<Set> getSets() {
        return databaseRealm.getRealmInstance()
                .where(Set.class)
                .findAll();
    }

    @Override
    public Set getSet(int setId) {
        return databaseRealm.getRealmInstance()
                .where(Set.class)
                .equalTo(Set.ID, setId)
                .findFirst();
    }

    @Override
    public RealmResults<Set> getSets(int exerciseId) {
        return databaseRealm.getRealmInstance()
                .where(Set.class)
                .equalTo("exercise.id", exerciseId)
                .findAllSorted(Set.DATE, Sort.DESCENDING);
    }

    @Override
    public Set getPreviousSet(Date date, int exerciseId) {
        RealmResults<Progress> progress = databaseRealm.getRealmInstance()
                .where(Progress.class)
                .lessThan(Progress.DATE, date)
                .beginGroup()
                    .equalTo("sets.exercise.id", exerciseId)
                .endGroup()
                .findAllSorted(Progress.DATE, Sort.DESCENDING);

        if (!progress.isEmpty())
            return progress.first().getSets().where().equalTo("exercise.id", exerciseId).findFirst();
        else
            return null;
    }

    @Override
    public void addSet(Progress progress, Set set) {
        try {
            databaseRealm.getRealmInstance().beginTransaction();

            if (set.getId() == 0)
                set.setId(getNextSetKey());

            progress.getSets().add(set);
            databaseRealm.getRealmInstance().commitTransaction();
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            databaseRealm.getRealmInstance().cancelTransaction();
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
            databaseRealm.getRealmInstance().cancelTransaction();
        }
    }

    @Override
    public void deleteSet(Set set) {
        try {
            databaseRealm.getRealmInstance().beginTransaction();
            databaseRealm.getRealmInstance()
                    .where(Set.class)
                    .equalTo(Set.ID, set.getId())
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
    public void updateSetOrder(Set set, int order) {
        try {
            databaseRealm.getRealmInstance().beginTransaction();
            set.setOrderId(order);
            databaseRealm.getRealmInstance().commitTransaction();
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            databaseRealm.getRealmInstance().cancelTransaction();
        }
    }

    @Override
    public void addRep(Set set, Rep rep) {
        try {
            databaseRealm.getRealmInstance().beginTransaction();

            if (rep.getId() == 0)
                rep.setId(getNextRepKey());

            set.getReps().add(rep);
            databaseRealm.getRealmInstance().copyToRealmOrUpdate(set);
            databaseRealm.getRealmInstance().commitTransaction();
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            databaseRealm.getRealmInstance().cancelTransaction();
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
            databaseRealm.getRealmInstance().cancelTransaction();
        }
    }

    @Override
    public void deleteRep(int repId) {
        try {
            databaseRealm.getRealmInstance().beginTransaction();
            databaseRealm.getRealmInstance()
                    .where(Rep.class)
                    .equalTo(Rep.ID, repId)
                    .findFirst()
                    .deleteFromRealm();
            databaseRealm.getRealmInstance().commitTransaction();
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            databaseRealm.getRealmInstance().cancelTransaction();
        }
    }
}
