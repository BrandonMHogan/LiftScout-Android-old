package com.brandonhogan.liftscout.foundation.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Progress extends RealmObject {

    public static final String ID = "id";
    public static final String WEIGHT = "WEIGHT";


    @PrimaryKey
    long id; // The long version of the date because realm cannot save date objects

    double weight;

    Date realDate;

    public Date getRealDate() {
        return realDate;
    }

    public void setRealDate(Date realDate) {
        this.realDate = realDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return new Date(id);
    }

    public void setDate(Date date) {
        this.id = date.getTime();
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
