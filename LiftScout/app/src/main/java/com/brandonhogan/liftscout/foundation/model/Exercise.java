package com.brandonhogan.liftscout.foundation.model;

import com.brandonhogan.liftscout.foundation.model.factory.RealmAutoIncrement;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Exercise extends RealmObject {


    @PrimaryKey
    private int id = RealmAutoIncrement.getInstance(this.getClass()).getNextIdFromModel();;
    private String name;
    private Category category;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
