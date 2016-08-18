package com.brandonhogan.liftscout.core.model;

import java.util.Calendar;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class User extends RealmObject {

    @PrimaryKey
    private int id;
    @Required
    private String name;
    @Required
    private Date birthDate;
    private double weight;
    @Required
    private String unitType;
    @Required
    private Date startDate;
    @Required
    private Date lastUsed;
    private boolean firstLoad = true;

    public User() {}

    public User(String name, Date birthDate, double weight, String unitType) {
        this.id = name.hashCode();
        this.name = name;
        this.birthDate = birthDate;
        this.weight = weight;
        this.unitType = unitType;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
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


    public Integer getAge() {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(birthDate);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        return age;
    }
}
