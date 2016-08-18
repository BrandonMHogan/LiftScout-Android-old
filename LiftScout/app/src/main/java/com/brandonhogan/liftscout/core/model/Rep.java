package com.brandonhogan.liftscout.core.model;

import com.brandonhogan.liftscout.core.model.factory.RealmAutoIncrement;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Rep extends RealmObject {

    public static final String ID = "id";
    public static final String COUNT = "count";
    public static final String WEIGHT = "weight";


    @PrimaryKey
    private int id = RealmAutoIncrement.getInstance(this.getClass()).getNextIdFromModel();

    private int count;
    private double weight;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
