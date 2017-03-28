package com.brandonhogan.liftscout.presenters;

import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.ExerciseDetailContract;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.repository.ExerciseRepo;
import com.brandonhogan.liftscout.repository.model.Exercise;
import com.brandonhogan.liftscout.utils.constants.ConstantValues;
import com.brandonhogan.liftscout.utils.constants.Measurements;

import javax.inject.Inject;


/**
 * Created by Brandon on 3/27/2017.
 * Description :
 */

public class ExerciseDetailPresenter implements ExerciseDetailContract.Presenter {

    @Inject
    ExerciseRepo exerciseRepo;

    @Inject
    UserManager userManager;


    // Private Properties
    //
    private ExerciseDetailContract.View view;
    private int exerciseId;
    private int categoryId;
    private boolean isNew;
    private Exercise exercise;


    // Constructor
    //
    public ExerciseDetailPresenter(ExerciseDetailContract.View view, boolean isNew, int exerciseId, int categoryId) {
        Injector.getAppComponent().inject(this);
        this.view = view;
        this.exerciseId = exerciseId;
        this.categoryId = categoryId;
        this.isNew = isNew;

        if (!isNew)
            exercise = exerciseRepo.getExercise(exerciseId);
    }

    @Override
    public void viewCreated() {
        if (!isNew) {
            view.setTitle(exercise.getName());
            view.setupControlValues(exercise);
        }
    }

    @Override
    public void onSave(String name, int increment, int restTimer, boolean isAuto, boolean isSound, boolean isVibrate) {

        if(isNew) {
            Exercise newExercise = new Exercise();
            newExercise.setName(name);
            newExercise.setIncrement(ConstantValues.increments.get(increment));
            newExercise.setRestVibrate(isVibrate);
            newExercise.setRestSound(isSound);
            newExercise.setRestAutoStart(isAuto);
            newExercise.setRestTimer(restTimer);
            newExercise.setCategoryId(categoryId);

            exerciseRepo.setExercise(newExercise);
        }
        else {
            exerciseRepo.updateExercise(exerciseId, name, ConstantValues.increments.get(increment), isVibrate, isSound, isAuto, restTimer);
        }

        view.onSaveSuccess();
    }

    @Override
    public double getDefaultIncrement() {
        if (userManager.getMeasurementValue().equals(Measurements.KILOGRAMS))
            return ConstantValues.INCREMENT_KG_DEFAULT;
        else
            return ConstantValues.INCREMENT_LB_DEFAULT;
    }
}
