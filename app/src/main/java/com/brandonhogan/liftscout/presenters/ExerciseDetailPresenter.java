package com.brandonhogan.liftscout.presenters;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.ExerciseDetailContract;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.repository.CategoryRepo;
import com.brandonhogan.liftscout.repository.ExerciseRepo;
import com.brandonhogan.liftscout.repository.model.Category;
import com.brandonhogan.liftscout.repository.model.Exercise;
import com.brandonhogan.liftscout.utils.constants.ConstantValues;
import com.brandonhogan.liftscout.utils.constants.Measurements;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmResults;


/**
 * Created by Brandon on 3/27/2017.
 * Description :
 */

public class ExerciseDetailPresenter implements ExerciseDetailContract.Presenter {

    @Inject
    ExerciseRepo exerciseRepo;

    @Inject
    UserManager userManager;

    @Inject
    CategoryRepo categoryRepo;


    // Private Properties
    //
    private ExerciseDetailContract.View view;
    private int exerciseId;
    private int categoryId;
    private boolean isNew, validCategoryId;
    private Exercise exercise;
    private RealmResults<Category> categories;
    private Category category;


    // Constructor
    //
    public ExerciseDetailPresenter(ExerciseDetailContract.View view, boolean isNew, int exerciseId, int categoryId, boolean validCategoryId) {
        Injector.getAppComponent().inject(this);
        this.view = view;
        this.exerciseId = exerciseId;
        this.categoryId = categoryId;
        this.isNew = isNew;
        this.validCategoryId = validCategoryId;

        categories = categoryRepo.getCategories();

        if (!isNew) {
            exercise = exerciseRepo.getExercise(exerciseId);
            category = categories.where().equalTo(Category.ID, exercise.getCategoryId()).findFirst();
        }
        else {
            category = categories.where().equalTo(Category.ID, categoryId).findFirst();
        }


    }

    public boolean validation(int id, String name, double increment, int restTimer, boolean isAuto, boolean isSound, boolean isVibrate) {
        Exercise exercise = new Exercise();
        exercise.setId(id);
        exercise.setName(name);
        exercise.setIncrement(increment);
        exercise.setRestTimer(restTimer);
        exercise.setRestAutoStart(isAuto);
        exercise.setRestSound(isSound);
        exercise.setRestVibrate(isVibrate);

        return validation(exercise);
    }

    public boolean validation(Exercise exercise) {
        if (exercise.getName().trim().isEmpty())
            view.onSaveFailure(R.string.exercise_edit_no_name);
        else if (exercise.getIncrement() == 0)
            view.onSaveFailure(R.string.exercise_edit_increment);
        else
            return true;

        return false;
    }

    @Override
    public void viewCreated() {
        if (!isNew) {
            view.setTitle(exercise.getName());
            view.setupControlValues(exercise);
        }

        view.setupControlCategory(category);
    }

    @Override
    public void onSave(int categoryPosition, String name, int increment, int restTimer, boolean isAuto, boolean isSound, boolean isVibrate) {

        if(isNew) {
            categoryId = categories.get(categoryPosition).getId();

            Exercise newExercise = new Exercise();
            newExercise.setName(name.trim());
            newExercise.setIncrement(ConstantValues.increments.get(increment));
            newExercise.setRestVibrate(isVibrate);
            newExercise.setRestSound(isSound);
            newExercise.setRestAutoStart(isAuto);
            newExercise.setRestTimer(restTimer);
            newExercise.setCategoryId(categoryId);

            if (validation(newExercise)) {
                exerciseRepo.setExercise(newExercise);
                view.onSaveSuccess();
            }
        }
        else if(validation(exerciseId, name, ConstantValues.increments.get(increment), restTimer, isAuto, isSound, isVibrate)) {
            exerciseRepo.updateExercise(exerciseId, name.trim(), ConstantValues.increments.get(increment), isVibrate, isSound, isAuto, restTimer);
            view.onSaveSuccess();
        }
    }

    @Override
    public ArrayList<String> getCategories() {
        ArrayList<String> stringCategories = new ArrayList<>();
        for (Category category : categories) {
            stringCategories.add(category.getName());
        }

        return stringCategories;
    }

    @Override
    public double getDefaultIncrement() {
        if (userManager.getMeasurementValue().equals(Measurements.KILOGRAMS))
            return ConstantValues.INCREMENT_KG_DEFAULT;
        else
            return ConstantValues.INCREMENT_LB_DEFAULT;
    }
}
