package com.brandonhogan.liftscout.views.workout.graph;

/**
 * Created by Brandon on 2/15/2017.
 * Description :
 */

public class GraphDataSet {
    private long id;
    private double value;

    public GraphDataSet(long id, double value) {
        this.id = id;
        this.value = value;
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
}
