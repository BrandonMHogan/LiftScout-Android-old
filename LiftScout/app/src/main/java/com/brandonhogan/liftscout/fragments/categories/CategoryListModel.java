package com.brandonhogan.liftscout.fragments.categories;

import com.brandonhogan.liftscout.foundation.model.Category;

public class CategoryListModel {

    private int id;
    private String name;
    private int color;

    public CategoryListModel(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.color = category.getColor();
    }

    public CategoryListModel() {

    }

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