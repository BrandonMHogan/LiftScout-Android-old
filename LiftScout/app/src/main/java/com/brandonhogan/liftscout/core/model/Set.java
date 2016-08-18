package com.brandonhogan.liftscout.core.model;

import com.brandonhogan.liftscout.core.model.factory.RealmAutoIncrement;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Set extends RealmObject {

    public static final String ID = "id";
    public static final String EXERCISE = "exercise";
    public static final String REPS = "reps";


    @PrimaryKey
    private int id = RealmAutoIncrement.getInstance(this.getClass()).getNextIdFromModel();

    private Exercise exercise;

    private RealmList<Rep> reps;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public RealmList<Rep> getReps() {
        return reps;
    }

    public void setReps(RealmList<Rep> reps) {
        this.reps = reps;
    }
}
