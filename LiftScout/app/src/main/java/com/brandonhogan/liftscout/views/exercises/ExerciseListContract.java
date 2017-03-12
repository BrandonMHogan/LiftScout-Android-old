package com.brandonhogan.liftscout.views.exercises;

import com.brandonhogan.liftscout.core.model.Exercise;

import java.util.ArrayList;

public interface ExerciseListContract {

    interface View {
        void applyTitle(String title);
        void updateAdapter(ArrayList<Exercise> data);
        void removeItem(int position);
        void itemSelected(int id, boolean isSearch);
        void swipeItem(int position);

    }

    interface Presenter {
        void viewCreated();
        void rowClicked(int position);
        void updateExercise(int id, String name, double increment, boolean vibrate, boolean sound, boolean autoStart, int restTimer);
        void createExercise(String name, double increment, boolean vibrate, boolean sound, boolean autoStart, int restTimer);
        void deleteExercise(int position);
        Exercise getExercise(int position);
        double getDefaultIncrement();
        boolean isInSearch();
    }
}
