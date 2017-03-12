package com.brandonhogan.liftscout.views.exercises;

import com.brandonhogan.liftscout.core.constants.ConstantValues;
import com.brandonhogan.liftscout.core.constants.Measurements;
import com.brandonhogan.liftscout.core.managers.GraphManager;
import com.brandonhogan.liftscout.core.managers.UserManager;
import com.brandonhogan.liftscout.core.model.Exercise;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.CategoryRepo;
import com.brandonhogan.liftscout.repository.ExerciseRepo;
import com.brandonhogan.liftscout.repository.impl.CategoryRepoImpl;
import com.brandonhogan.liftscout.repository.impl.ExerciseRepoImpl;

import java.util.ArrayList;

import javax.inject.Inject;

import io.realm.RealmResults;

public class ExerciseListPresenter implements ExerciseListContract.Presenter {

    @Inject
    GraphManager graphManager;

    @Inject
    UserManager userManager;

    // Private Properties
    //
    private ExerciseListContract.View view;
    private int categoryId;
    private boolean isAddSet;
    private ArrayList<Exercise> adapterData;
    private ExerciseRepo exerciseRepo;


    // Constructor
    //
    public ExerciseListPresenter(ExerciseListContract.View view, int categoryId, boolean isAddSet) {
        Injector.getAppComponent().inject(this);
        this.view = view;
        this.categoryId = categoryId;
        this.isAddSet = isAddSet;

        exerciseRepo = new ExerciseRepoImpl();
    }


    // Private Functions
    //
    private void updateAdapter() {
        adapterData = new ArrayList<>();

        RealmResults<Exercise> exercises = exerciseRepo.getExercises(categoryId, false).sort(Exercise.NAME);

        if (exercises != null)
            adapterData.addAll(exercises);

        view.updateAdapter(adapterData);
    }


    // Contracts
    //
    @Override
    public void viewCreated() {

        updateAdapter();

        CategoryRepo categoryRepo = new CategoryRepoImpl();
        view.applyTitle(categoryRepo.getCategory(categoryId).getName());
    }

    @Override
    public boolean isInSearch() {
        return graphManager.isInSearch();
    }

    @Override
    public void rowClicked(int position) {

        // Since we are searching for the id, store it now into the manager
        if (graphManager.isInSearch()) {
            graphManager.setCurrentExerciseId(adapterData.get(position).getId());
        }

        if (isAddSet || graphManager.isInSearch()) {
            view.itemSelected(adapterData.get(position).getId(), graphManager.isInSearch());
        }
        else {
            view.swipeItem(position);
        }
    }

    @Override
    public void createExercise(String name, double increment, boolean vibrate, boolean sound, boolean autoStart, int restTimer) {
        Exercise newExercise = new Exercise();
        newExercise.setName(name);
        newExercise.setIncrement(increment);
        newExercise.setRestVibrate(vibrate);
        newExercise.setRestSound(sound);
        newExercise.setRestAutoStart(autoStart);
        newExercise.setRestTimer(restTimer);
        newExercise.setCategoryId(categoryId);

        exerciseRepo.setExercise(newExercise);
        updateAdapter();
    }

    @Override
    public void updateExercise(int id, String name, double increment, boolean vibrate, boolean sound, boolean autoStart, int restTimer) {
        exerciseRepo.updateExercise(id, name, increment, vibrate, sound, autoStart, restTimer);
        updateAdapter();
    }

    @Override
    public void deleteExercise(int position) {
        exerciseRepo.deleteExercise(adapterData.get(position).getId());
        adapterData.remove(position);
        view.removeItem(position);
        updateAdapter();
    }

    @Override
    public Exercise getExercise(int position) {
        return adapterData.get(position);
    }

    @Override
    public double getDefaultIncrement() {
        if (userManager.getMeasurementValue().equals(Measurements.KILOGRAMS))
            return ConstantValues.INCREMENT_KG_DEFAULT;
        else
            return ConstantValues.INCREMENT_LB_DEFAULT;
    }
}
