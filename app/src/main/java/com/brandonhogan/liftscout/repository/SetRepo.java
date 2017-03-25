package com.brandonhogan.liftscout.repository;

import com.brandonhogan.liftscout.repository.model.Progress;
import com.brandonhogan.liftscout.repository.model.Rep;
import com.brandonhogan.liftscout.repository.model.Set;

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
    Observable<Set> getSet(int setId);

    Observable<Boolean> addRep(Set set, Rep rep);
    void updateRep(Rep rep);
    void deleteRep(int repId);
    RealmResults<Set> getSets(int exerciseId);
    RealmResults<Set> getSets();
}
