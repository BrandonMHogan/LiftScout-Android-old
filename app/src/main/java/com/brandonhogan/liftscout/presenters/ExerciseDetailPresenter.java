package com.brandonhogan.liftscout.presenters;

import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.ExerciseDetailContract;
import com.brandonhogan.liftscout.repository.ExerciseRepo;
import com.brandonhogan.liftscout.repository.model.Exercise;

import javax.inject.Inject;


/**
 * Created by Brandon on 3/27/2017.
 * Description :
 */

public class ExerciseDetailPresenter implements ExerciseDetailContract.Presenter {

    @Inject
    ExerciseRepo exerciseRepo;


    // Private Properties
    //
    private ExerciseDetailContract.View view;
    private int exerciseId;
    private boolean isNew;
    private Exercise exercise;


    // Constructor
    //
    public ExerciseDetailPresenter(ExerciseDetailContract.View view, boolean isNew, int exerciseId) {
        Injector.getAppComponent().inject(this);
        this.view = view;
        this.exerciseId = exerciseId;
        this.isNew = isNew;

        exercise = exerciseRepo.getExercise(exerciseId);
    }

    @Override
    public void viewCreated() {
        if (!isNew) {
            view.setTitle(exercise.getName());
        }
    }
}
