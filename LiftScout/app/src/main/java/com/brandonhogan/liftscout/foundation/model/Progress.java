package com.brandonhogan.liftscout.foundation.model;

import com.brandonhogan.liftscout.foundation.utils.BhDate;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Progress extends RealmObject {

    public static final String ID = "id";
    public static final String DATE = "date";
    public static final String WEIGHT = "weight";


    @PrimaryKey
    long id;

    @Required
    Date date;

    double weight;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = BhDate.trimTimeFromDate(date);
        this.id = date.getTime();
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
