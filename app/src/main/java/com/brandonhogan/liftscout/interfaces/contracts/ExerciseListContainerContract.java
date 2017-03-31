package com.brandonhogan.liftscout.interfaces.contracts;

/**
 * Created by Brandon on 3/29/2017.
 * Description :
 */

public interface ExerciseListContainerContract {
    interface View {
        void onNoCategoryFound();
        void onCreateExercise();
        void onCreateCategory();
    }

    interface Presenter {
        void viewCreated();
        void onDestroyView();
        boolean isAddSet();
        void onFabClicked(int position);
    }
}
