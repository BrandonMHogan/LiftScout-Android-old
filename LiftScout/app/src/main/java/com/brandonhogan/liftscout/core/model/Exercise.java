package com.brandonhogan.liftscout.core.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Exercise extends RealmObject {

    public static final String ID = "id";
    public static final String CATEGORY_ID = "categoryId";
    public static final String NAME = "name";
    public static final String REST_TIMER = "restTimer";
    public static final String REST_VIBRATE = "restVibrate";


    @PrimaryKey
    private int id;

    private int categoryId;

    @Required
    private String name;

    private int type;

    private int restTimer = 60;

    private boolean restVibrate = true;

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
}
