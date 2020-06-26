package com.brandonhogan.liftscout.repository.model;

import androidx.annotation.NonNull;

import com.brandonhogan.liftscout.utils.constants.ConstantValues;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Exercise extends RealmObject {

    public static final String ID = "id";
    public static final String CATEGORY_ID = "categoryId";
    public static final String NAME = "name";
    public static final String IS_DELETED = "isDeleted";
    public static final String DELETE_DATE = "deleteDate";
    public static final String REST_TIMER = "restTimer";
    public static final String REST_VIBRATE = "restVibrate";
    public static final String REST_SOUND = "restSound";
    public static final String REST_AUTO_START = "restAutoStart";
    public static final String INCREMENT = "increment";
    public static final String ALLOW_NEGATIVE_VALUE = "allowNegativeValue";
    public static final String FAVOURITE = "favourite";


    @PrimaryKey
    private int id;

    private int categoryId;

    @Required
    private String name;

    private int type;

    private boolean isDeleted;

    private Date deleteDate;

    private int restTimer = ConstantValues.REST_TIME_DEFAULT;

    private boolean restSound = true;

    private boolean restVibrate = true;

    private boolean restAutoStart = false;

    private double increment;

    private boolean favourite = false;

    @NonNull
    private boolean allowNegativeValue = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    public int getRestTimer() {
        return restTimer;
    }

    public void setRestTimer(int restTimer) {
        this.restTimer = restTimer;
    }

    public boolean isRestVibrate() {
        return restVibrate;
    }

    public void setRestVibrate(boolean restVibrate) {
        this.restVibrate = restVibrate;
    }

    public boolean isRestSound() {
        return restSound;
    }

    public void setRestSound(boolean restSound) {
        this.restSound = restSound;
    }

    public boolean isRestAutoStart() {
        return restAutoStart;
    }

    public void setRestAutoStart(boolean restAutoStart) {
        this.restAutoStart = restAutoStart;
    }

    public double getIncrement() {
        return increment;
    }

    public void setIncrement(double increment) {
        this.increment = increment;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public boolean isAllowNegativeValue() {
        return allowNegativeValue;
    }

    public void setAllowNegativeValue(boolean allowNegativeValue) {
        this.allowNegativeValue = allowNegativeValue;
    }
}
