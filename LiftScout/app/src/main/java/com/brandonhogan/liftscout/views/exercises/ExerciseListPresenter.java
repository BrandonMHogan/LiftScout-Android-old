package com.brandonhogan.liftscout.views.exercises;

import com.brandonhogan.liftscout.core.managers.GraphManager;
import com.brandonhogan.liftscout.core.model.Category;
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

    // Private Properties
    //
    private ExerciseListContract.View view;
    private int categoryId;
    private boolean isAddSet;
    private ArrayList<ExerciseListModel> adapterData;
    ExerciseRepo exerciseRepo;


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
        if (adapterData != null)
            view.updateAdapter(adapterData);

        adapterData = new ArrayList<>();

        RealmResults<Exercise> exercises = exerciseRepo.getExercises(categoryId, false);

        if (exercises == null)
            view.updateAdapter(adapterData);


        for (Exercise exercise : exercises.sort(Category.NAME)) {
            adapterData.add(new ExerciseListModel(exercise));
        }

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
    public void createExercise(ExerciseListModel exerciseListModel) {
        Exercise newExercise = new Exercise();
        newExercise.setName(exerciseListModel.getName());
        newExercise.setCategoryId(categoryId);

        exerciseRepo.setExercise(newExercise);
        updateAdapter();
    }

    @Override
    public void updateExercise(ExerciseListModel exerciseListModel) {
        Exercise newExercise = new Exercise();
        newExercise.setName(exerciseListModel.getName());
        newExercise.setCategoryId(categoryId);
        newExercise.setId(exerciseListModel.getId());

        exerciseRepo.setExercise(newExercise);
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
    public ExerciseListModel getExercise(int position) {
        return adapterData.get(position);
    }
}
