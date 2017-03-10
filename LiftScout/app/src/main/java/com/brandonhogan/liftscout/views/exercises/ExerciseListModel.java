package com.brandonhogan.liftscout.views.exercises;

import com.brandonhogan.liftscout.core.constants.ConstantValues;
import com.brandonhogan.liftscout.core.model.Exercise;

public class ExerciseListModel {
    private int id;
    private String name;
    private int restTimer;
    private boolean restVibrate;
    private boolean restSound;
    private boolean restAutoStart;
    private double increment;

    public ExerciseListModel(Exercise exercise) {
        this.id = exercise.getId();
        this.name = exercise.getName();
        this.restTimer = exercise.getRestTimer() == 0 ? ConstantValues.REST_TIME_DEFAULT : restTimer;
        this.restVibrate = exercise.isRestVibrate();
        this.restSound = exercise.isRestSound();
        this.restAutoStart = exercise.isRestAutoStart();
        this.increment = exercise.getIncrement();
    }

    public ExerciseListModel() {
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

    public int getRestTimer() {
        return restTimer;
    }

    public void setRestTimer(int restTimer) {
        this.restTimer = restTimer;
    }

    public boolean isRestVibrate() {
        return restVibrate;
    }

    public void setRestVibrate(boolean restVibrate) {
        this.restVibrate = restVibrate;
    }

    public boolean isRestSound() {
        return restSound;
    }

    public void setRestSound(boolean restSound) {
        this.restSound = restSound;
    }

    public boolean isRestAutoStart() {
        return restAutoStart;
    }

    public void setRestAutoStart(boolean restAutoStart) {
        this.restAutoStart = restAutoStart;
    }

    public double getIncrement() {
        return increment;
    }

    public void setIncrement(double increment) {
        this.increment = increment;
    }
}
