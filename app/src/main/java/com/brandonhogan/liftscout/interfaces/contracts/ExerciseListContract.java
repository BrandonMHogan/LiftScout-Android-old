package com.brandonhogan.liftscout.interfaces.contracts;

import com.brandonhogan.liftscout.repository.model.Exercise;
import com.brandonhogan.liftscout.models.ExerciseListModel;

import java.util.ArrayList;

public interface ExerciseListContract {

    interface View {
        void applyTitle(String title);
        void updateAdapter(ArrayList<ExerciseListModel> data);
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
        int getCategoryId();
        double getDefaultIncrement();
        boolean isInSearch();
    }
}
