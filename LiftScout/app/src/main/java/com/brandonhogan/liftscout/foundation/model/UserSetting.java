package com.brandonhogan.liftscout.foundation.model;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class UserSetting extends RealmObject {

    public static final String NAME = "name";
    public static final String VALUE = "value";


    // Types
    public static final String THEME = "theme";

//    @Ignore
//    public static final ArrayList<String> TodayTransformType = new ArrayList<String>() {{
//                add("Scale In Out");
//                add("B");
//                add("C");
//            }};

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

    public void setValue(String value) {
        this.value = value;
    }
}
