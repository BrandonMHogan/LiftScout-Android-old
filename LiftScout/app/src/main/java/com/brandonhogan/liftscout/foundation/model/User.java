package com.brandonhogan.liftscout.foundation.model;

import java.util.Date;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {

    @PrimaryKey
    private int id;
    private String name;
    private int age;
    private double weight;
    private Date startDate;
    private Date lastUsed;
    private boolean firstLoad = true;

    public User() {}

    public User(String name, int age, double weight) {
        this.id = name.hashCode();
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.startDate = new Date();
        this.lastUsed = this.startDate;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Date lastUsed) {
        this.lastUsed = lastUsed;
    }

    public boolean isFirstLoad() {
        return firstLoad;
    }

    public void setFirstLoad(boolean firstLoad) {
        this.firstLoad = firstLoad;
    }
}
