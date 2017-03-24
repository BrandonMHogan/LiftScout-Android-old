package com.brandonhogan.liftscout.presenters;

import com.brandonhogan.liftscout.interfaces.contracts.GraphExercisesContract;
import com.brandonhogan.liftscout.managers.GraphManager;
import com.brandonhogan.liftscout.managers.ProgressManager;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.CategoryRepo;
import com.brandonhogan.liftscout.repository.ExerciseRepo;
import com.brandonhogan.liftscout.repository.impl.CategoryRepoImpl;
import com.brandonhogan.liftscout.repository.impl.ExerciseRepoImpl;

import javax.inject.Inject;

/**
 * Created by Brandon on 3/1/2017.
 * Description :
 */

public class GraphExercisesPresenter implements GraphExercisesContract.Presenter {

    // Injections
    //
    @Inject
    ProgressManager progressManager;

    @Inject
    GraphManager graphManager;

    @Inject
    UserManager userManager;

    // Private Properties
    //
    private GraphExercisesContract.View view;
    private CategoryRepo categoryRepo;
    private ExerciseRepo exerciseRepo;
    private int currentExerciseId;

    // Constructor
    //
    public GraphExercisesPresenter(GraphExercisesContract.View view) {
        Injector.getAppComponent().inject(this);

        this.view = view;
        categoryRepo = new CategoryRepoImpl();
        exerciseRepo = new ExerciseRepoImpl();
    }

    @Override
    public void viewCreated() {

    }

    @Override
    public void onResume() {

        // If we were in search, then collect the id and clear the search
        if (graphManager.isInSearch()) {
            graphManager.setInSearch(false);


            // If nothing is selected, then don't try and set an exercise
            if (!graphManager.isSelectionMade())
                return;

            currentExerciseId = graphManager.getCurrentExerciseId();
            view.setSelectedExercise(currentExerciseId, exerciseRepo.getExercise(currentExerciseId).getName());
        }
    }
}
