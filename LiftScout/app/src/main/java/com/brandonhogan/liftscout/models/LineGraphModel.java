package com.brandonhogan.liftscout.models;

/**
 * Created by Brandon on 2/15/2017.
 * Description :
 */

public class LineGraphModel {
    private long id;
    private double value;
    private double weight;
    private int reps;

    public LineGraphModel(long id, double value) {
        this.id = id;
        this.value = value;
        this.weight = 0;
        this.reps = 0;
    }

    public LineGraphModel(long id, double value, double weight, int reps) {
        this.id = id;
        this.value = value;
        this.weight = weight;
        this.reps = reps;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }
}
