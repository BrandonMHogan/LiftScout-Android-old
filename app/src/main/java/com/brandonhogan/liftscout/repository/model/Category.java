package com.brandonhogan.liftscout.repository.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Category extends RealmObject {

    // Static Property Names
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String COLOR = "color";
    public static final String IS_DELETED = "isDeleted";
    public static final String DELETE_DATE = "deleteDate";
    public static final String DELETABLE = "deletable";


    @PrimaryKey
    private int id;
    @Required
    private String name;
    private int color;
    private boolean isDeleted;
    private boolean deletable;
    private Date deleteDate;

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

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }
}
