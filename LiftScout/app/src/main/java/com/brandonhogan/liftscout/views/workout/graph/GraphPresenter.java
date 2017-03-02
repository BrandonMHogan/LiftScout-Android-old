package com.brandonhogan.liftscout.views.workout.graph;

import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.core.managers.UserManager;
import com.brandonhogan.liftscout.core.model.Exercise;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.ExerciseRepo;
import com.brandonhogan.liftscout.repository.impl.ExerciseRepoImpl;

import javax.inject.Inject;

/**
 * Created by Brandon on 2/15/2017.
 * Description :
 */

public class GraphPresenter implements GraphContract.Presenter {

    // Injections
    //
    @Inject
    ProgressManager progressManager;

    @Inject
    UserManager userManager;

    // Private Properties
    //
    private GraphContract.View view;
    private int exerciseId;


    // Constructor
    //
    public GraphPresenter(GraphContract.View view, int exerciseId) {
        Injector.getAppComponent().inject(this);

        this.view = view;
        this.exerciseId = exerciseId;

    }

    @Override
    public void viewCreated() {
        ExerciseRepo exerciseRepo = new ExerciseRepoImpl();
        Exercise exercise = exerciseRepo.getExercise(exerciseId);

        if (exercise.isValid())
            view.setupGraph(exerciseId, exercise.getName());
    }
}
