package com.brandonhogan.liftscout.repository;

import com.brandonhogan.liftscout.core.model.Progress;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;

public interface SetRepo {

    void addSet(Progress progress, Set set);
    void updateSet(Set set);
    void deleteSet(Set set);
    void updateSetOrder(Set set, int order);

    void addRep(Set set, Rep rep);
    void updateRep(Rep rep);
    Set getSet(int setId);
}
