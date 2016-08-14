package com.brandonhogan.liftscout.core.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class UserSetting extends RealmObject {

    public static final String NAME = "name";
    public static final String VALUE = "value";


    // Types
    public static final String THEME = "theme";
    public static final String TODAY_TRANSFORM = "todayTransform";
    public static final String TODAY_SHOW_WEIGHT = "today_showWeight";
    public static final String TODAY_SHOW_PHOTO = "today_showPhoto";
    public static final String TODAY_SHOW_ROUTINE = "today_showRoutine";


    @PrimaryKey
    @Required
    private String name;
    @Required
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public boolean getValueBoolean() {
        return value.equals("1");
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setValue(boolean value) {
        this.value = value ? "1" : "0";
    }
}
