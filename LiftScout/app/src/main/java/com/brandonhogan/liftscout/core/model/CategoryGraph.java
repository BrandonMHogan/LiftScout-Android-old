package com.brandonhogan.liftscout.core.model;

/**
 * Created by Brandon on 2/28/2017.
 * Description :
 */

public class CategoryGraph {
    private String name;
    private int value;
    private int color;

    public CategoryGraph(String name, int value, int color) {
        this.name = name;
        this.value = value;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
