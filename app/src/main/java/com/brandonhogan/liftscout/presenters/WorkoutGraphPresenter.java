package com.brandonhogan.liftscout.presenters;

import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.GraphContract;
import com.brandonhogan.liftscout.managers.ProgressManager;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.repository.ExerciseRepo;
import com.brandonhogan.liftscout.repository.impl.ExerciseRepoImpl;
import com.brandonhogan.liftscout.repository.model.Exercise;

import javax.inject.Inject;

/**
 * Created by Brandon on 2/15/2017.
 * Description :
 */

public class WorkoutGraphPresenter implements GraphContract.Presenter {

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
    public WorkoutGraphPresenter(GraphContract.View view, int exerciseId) {
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
