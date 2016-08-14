package com.brandonhogan.liftscout.fragments.home.workout;

public class TodayItem {

    private final int id;
    private final int reps;
    private final double weight;

    public TodayItem(int id, int reps, double weight) {
        this.id = id;
        this.reps = reps;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public int getReps() {
        return reps;
    }

    public double getWeight() {
        return weight;
    }
}
