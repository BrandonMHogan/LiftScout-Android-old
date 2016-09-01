package com.brandonhogan.liftscout.views.exercises;

import java.util.List;

public interface ExerciseListContract {

    interface View {
        void applyTitle(String title);
        void updateAdapter(List<ExerciseListModel> data);
        void itemSelected(int id);
        void swipeItem(int position);
    }

    interface Presenter {
        void viewCreated();
        void rowClicked(int position);
        void updateExercise(ExerciseListModel exerciseListModel);
        void createExercise(ExerciseListModel exerciseListModel);
        void deleteExercise(int position);
        ExerciseListModel getExercise(int position);
    }
}
