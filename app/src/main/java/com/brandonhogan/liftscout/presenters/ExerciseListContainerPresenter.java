package com.brandonhogan.liftscout.presenters;

import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.ExerciseListContainerContract;
import com.brandonhogan.liftscout.repository.CategoryRepo;

import javax.inject.Inject;

/**
 * Created by Brandon on 3/29/2017.
 * Description :
 */

public class ExerciseListContainerPresenter implements ExerciseListContainerContract.Presenter {

    @Inject
    CategoryRepo categoryRepo;

    boolean addSet;

    // Private Properties
    //
    private ExerciseListContainerContract.View view;

    // Constructor
    //
    public ExerciseListContainerPresenter(ExerciseListContainerContract.View view, boolean addSet) {
        Injector.getAppComponent().inject(this);

        this.view = view;
        this.addSet = addSet;
    }

    @Override
    public void viewCreated() {

    }

    @Override
    public void onDestroyView() {
        this.view = null;
    }

    @Override
    public boolean isAddSet() {
        return addSet;
    }

    @Override
    public void onFabClicked(int position) {

        switch (position) {
            case 0:

                if (categoryRepo.getCategories().isEmpty()) {
                    view.onNoCategoryFound();
                }
                else {
                    view.onCreateExercise();
                }
                break;
            case 1:
                view.onCreateCategory();
                break;
        }
    }
}
