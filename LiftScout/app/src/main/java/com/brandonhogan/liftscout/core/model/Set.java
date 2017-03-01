package com.brandonhogan.liftscout.core.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Set extends RealmObject {

    public static final String ID = "id";
    public static final String ORDER_ID = "orderId";
    public static final String EXERCISE = "exercise";
    public static final String REPS = "reps";
    public static final String DATE = "date";


    @PrimaryKey
    private int id;

    private int orderId = 0;

    private Exercise exercise;

    private RealmList<Rep> reps;

    @Index
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
