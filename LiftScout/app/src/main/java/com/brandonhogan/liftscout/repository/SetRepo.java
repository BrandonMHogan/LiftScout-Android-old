package com.brandonhogan.liftscout.repository;

import com.brandonhogan.liftscout.core.model.Progress;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmResults;

public interface SetRepo {

    void addSet(Progress progress, Set set);
    void updateSet(Set set);
    void deleteSet(Set set);
    void updateSetOrder(Set set, int order);
    Set getPreviousSet(Date date, int exerciseId);

    void addRep(Set set, Rep rep);
    void updateRep(Rep rep);
    void deleteRep(int repId);
    Set getSet(int setId);
    RealmResults<Set> getSets(int exerciseId);
}
