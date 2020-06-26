package com.brandonhogan.liftscout.repository.impl;

import android.util.Log;

import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.DatabaseRealm;
import com.brandonhogan.liftscout.repository.SetRepo;
import com.brandonhogan.liftscout.repository.model.Category;
import com.brandonhogan.liftscout.repository.model.Progress;
import com.brandonhogan.liftscout.repository.model.Rep;
import com.brandonhogan.liftscout.repository.model.Set;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
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
    public RealmResults<Set> getSets(int exerciseId) {
        return databaseRealm.getRealmInstance()
                .where(Set.class)
                .equalTo("exercise.id", exerciseId).sort(Set.DATE, Sort.DESCENDING)
                .findAll();
    }

    @Override
    public Set getPreviousSet(Date date, int exerciseId) {
        RealmResults<Progress> progress = databaseRealm.getRealmInstance()
                .where(Progress.class)
                .lessThan(Progress.DATE, date)
                .beginGroup()
                    .equalTo("sets.exercise.id", exerciseId)
                .endGroup()
                .sort(Set.DATE, Sort.DESCENDING)
                .findAll();

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
    public void deleteSet(int setId) {
        try {
            databaseRealm.getRealmInstance().beginTransaction();
            databaseRealm.getRealmInstance()
                    .where(Set.class)
                    .equalTo(Set.ID, setId)
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
    public Observable<Boolean> addRep(final Set set, final Rep rep) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                try {
                    databaseRealm.getRealmInstance().beginTransaction();

                    if (rep.getId() == 0)
                        rep.setId(getNextRepKey());

                    set.getReps().add(rep);
                    databaseRealm.getRealmInstance().copyToRealmOrUpdate(set);
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

    @Override
    public Observable<Rep> getRep(final int repId) {
        return Observable.create(new ObservableOnSubscribe<Rep>() {
            @Override
            public void subscribe(ObservableEmitter<Rep> e) throws Exception {
                try {
                    Rep rep = databaseRealm.getRealmInstance()
                            .where(Rep.class)
                            .equalTo(Rep.ID, repId)
                            .findFirst();

                    e.onNext(rep);
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
    public Observable<Set> getSet(final int setId) {
        return Observable.create(new ObservableOnSubscribe<Set>() {
            @Override
            public void subscribe(ObservableEmitter<Set> e) throws Exception {
                try {
                    Set set = databaseRealm.getRealmInstance()
                            .where(Set.class)
                            .equalTo(Set.ID, setId)
                            .findFirst();

                    e.onNext(set);
                    e.onComplete();
                }
                catch (Exception ex) {
                    Log.e(TAG, "subscribe: getSet", ex);
                    e.onError(ex);
                }
            }
        });
    }
}
