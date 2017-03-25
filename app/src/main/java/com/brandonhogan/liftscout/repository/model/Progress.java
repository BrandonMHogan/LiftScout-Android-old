package com.brandonhogan.liftscout.repository.model;

import com.brandonhogan.liftscout.utils.BhDate;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Progress extends RealmObject {

    public static final String ID = "id";
    public static final String DATE = "date";
    public static final String WEIGHT = "weight";
    public static final String SETS = "sets";


    @PrimaryKey
    private long id;

    @Required
    private Date date;

    private double weight;

    private RealmList<Set> sets;

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

    public RealmList<Set> getSets() {
        return sets;
    }

    public void setSets(RealmList<Set> sets) {
        this.sets = sets;
    }
}
