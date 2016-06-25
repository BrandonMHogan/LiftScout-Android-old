package com.brandonhogan.liftscout.foundation.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Exercise extends RealmObject {


    @PrimaryKey
    private int id;
    private String name;
    private ExerciseCategory category;
    private int type;

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

    public ExerciseCategory getCategory() {
        return category;
    }

    public void setCategory(ExerciseCategory category) {
        this.category = category;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
