package com.brandonhogan.liftscout.aaadev;

import com.brandonhogan.liftscout.core.model.Exercise;
import com.brandonhogan.liftscout.core.model.Progress;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;

import io.realm.Realm;
import io.realm.RealmList;

public class AAADevWorkout {

    public static final void clearSets(Realm realm) {
        realm.beginTransaction();
        realm.where(Set.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

    public static final void addSet(Realm realm, Progress progress, int position) {
        realm.beginTransaction();

        Exercise exercise = realm.where(Exercise.class).findAll().get(position);

        Set set = new Set();
        set.setExercise(exercise);
            Rep rep1 = new Rep();
            rep1.setWeight(225);
            rep1.setCount(5);
            Rep rep2 = new Rep();
            rep2.setWeight(225);
            rep2.setCount(5);
            Rep rep3 = new Rep();
            rep3.setWeight(225);
            rep3.setCount(5);
        Rep rep4 = new Rep();
        rep4.setWeight(225);
        rep4.setCount(5);
        Rep rep5 = new Rep();
        rep5.setWeight(225);
        rep5.setCount(5);

            RealmList<Rep> reps = new RealmList<>();
            reps.add(rep1);
            reps.add(rep2);
            reps.add(rep3);
            reps.add(rep4);
            reps.add(rep5);
        set.setReps(reps);

        progress.getSets().add(set);
        realm.copyToRealmOrUpdate(progress);
        realm.commitTransaction();
    }
}
