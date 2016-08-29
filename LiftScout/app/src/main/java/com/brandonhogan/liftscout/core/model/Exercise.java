package com.brandonhogan.liftscout.core.model;

import com.brandonhogan.liftscout.core.model.factory.RealmAutoIncrement;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Exercise extends RealmObject {

    public static final String ID = "id";
    public static final String CATEGORY_ID = "categoryId";
    public static final String NAME = "name";


    @PrimaryKey
    private int id;

    private int categoryId;

    @Required
    private String name;

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
}
