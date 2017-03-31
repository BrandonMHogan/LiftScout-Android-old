package com.brandonhogan.liftscout.interfaces.contracts;

import com.brandonhogan.liftscout.models.ExerciseListModel;
import com.brandonhogan.liftscout.repository.model.Exercise;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public interface ExerciseListContract {

    interface View {
        void applyTitle(String title, boolean favOnly, boolean showAll);
        void updateAdapter(ArrayList<ExerciseListModel> data);
        void removeItem(int position);
        void itemSelected(int id, boolean isSearch);
        void swipeItem(int position);

    }

    interface Presenter {
        void viewCreated(int categoryId, boolean favOnly, boolean showAll, boolean isAddSet);
        void onDestroy();
        void onResume(ExerciseListContract.View view);
        void updateAdapter();
        void rowClicked(int position);
        void deleteExercise(int position);
        Exercise getExercise(int position);
        int getCategoryId();
        boolean isInSearch();
        boolean isShowAll();
        boolean isFavOnly();
    }
}
