package com.brandonhogan.liftscout.foundation.model;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class UserSettings extends RealmObject {

//    @Ignore
//    public static final ArrayList<String> TodayTransformType = new ArrayList<String>() {{
//                add("Scale In Out");
//                add("B");
//                add("C");
//            }};

    @PrimaryKey
    private int id;
    @Required
    private String name;
    @Required
    private String value;


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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
