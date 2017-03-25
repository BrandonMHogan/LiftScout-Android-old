package com.brandonhogan.liftscout.repository.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Category extends RealmObject {

    // Static Property Names
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String COLOR = "color";


    @PrimaryKey
    private int id;
    @Required
    private String name;
    private int color;

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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
