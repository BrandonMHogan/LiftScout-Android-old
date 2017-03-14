package com.brandonhogan.liftscout.core.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class Rep extends RealmObject {

    public static final String ID = "id";
    public static final String COUNT = "count";
    public static final String WEIGHT = "weight";


    @PrimaryKey
    private int id;

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
