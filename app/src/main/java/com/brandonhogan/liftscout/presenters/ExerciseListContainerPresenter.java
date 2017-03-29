package com.brandonhogan.liftscout.presenters;

import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.ExerciseListContainerContract;

/**
 * Created by Brandon on 3/29/2017.
 * Description :
 */

public class ExerciseListContainerPresenter implements ExerciseListContainerContract.Presenter {

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
}
