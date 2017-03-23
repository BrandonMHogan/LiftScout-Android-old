package com.brandonhogan.liftscout.views.exercises;

import com.brandonhogan.liftscout.core.model.Exercise;

public class ExerciseListModel{

    private int id;
    private String name;

    public ExerciseListModel(Exercise exercise) {
        this.id = exercise.getId();
        this.name = exercise.getName();
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

}
