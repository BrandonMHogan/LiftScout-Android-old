package com.brandonhogan.liftscout.repository;

import com.brandonhogan.liftscout.core.model.Progress;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;

import java.util.Date;

import io.reactivex.Observable;
import io.realm.RealmResults;

public interface SetRepo {

    void addSet(Progress progress, Set set);
    void updateSet(Set set);
    void deleteSet(Set set);
    void deleteSet(int setId);
    void updateSetOrder(Set set, int order);
    Set getPreviousSet(Date date, int exerciseId);

    Observable<Rep> getRep(int repId);
    void addRep(Set set, Rep rep);
    void updateRep(Rep rep);
    void deleteRep(int repId);
    Set getSet(int setId);
    RealmResults<Set> getSets(int exerciseId);
    RealmResults<Set> getSets();
}
