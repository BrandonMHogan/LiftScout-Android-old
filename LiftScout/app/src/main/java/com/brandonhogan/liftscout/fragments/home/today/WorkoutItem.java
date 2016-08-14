package com.brandonhogan.liftscout.fragments.home.today;

public class WorkoutItem {

    private int mReps;
    private double mWeight;

    public WorkoutItem(int reps, double weight) {
        mReps = reps;
        mWeight = weight;
    }

    public int getmReps() {
        return mReps;
    }

    public double getmWeight() {
        return mWeight;
    }
}
