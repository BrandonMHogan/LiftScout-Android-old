package com.brandonhogan.liftscout.presenters;

import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.ExerciseListContract;
import com.brandonhogan.liftscout.managers.ExerciseManager;
import com.brandonhogan.liftscout.managers.GraphManager;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.models.ExerciseListModel;
import com.brandonhogan.liftscout.repository.CategoryRepo;
import com.brandonhogan.liftscout.repository.ExerciseRepo;
import com.brandonhogan.liftscout.repository.impl.CategoryRepoImpl;
import com.brandonhogan.liftscout.repository.impl.ExerciseRepoImpl;
import com.brandonhogan.liftscout.repository.model.Exercise;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ExerciseListPresenter implements ExerciseListContract.Presenter {

    @Inject
    GraphManager graphManager;

    @Inject
    UserManager userManager;

    @Inject
    ExerciseManager exerciseManager;

    // Private Properties
    //
    private ExerciseListContract.View view;
    private int categoryId;
    private boolean isAddSet, favOnly, showAll;
    private ArrayList<Exercise> adapterData;
    private ExerciseRepo exerciseRepo;
    private Disposable disposable;

    // Constructor
    //
    public ExerciseListPresenter(ExerciseListContract.View view) {
        Injector.getAppComponent().inject(this);
        this.view = view;

        exerciseRepo = new ExerciseRepoImpl();
    }

    @Override
    public void onResume(ExerciseListContract.View view) {
        this.view = view;
        setupExerciseListener();

        CategoryRepo categoryRepo = new CategoryRepoImpl();

        if (favOnly || showAll)
            view.applyTitle(null, favOnly, showAll);
        else {
            view.applyTitle(categoryRepo.getCategory(categoryId).getName(), favOnly, showAll);
            view.showFab();
        }

        updateAdapter();
    }

    @Override
    public void onDestroy() {
        this.disposable.dispose();
    }

    // Private Functions
    //

    private void setupExerciseListener() {
        disposable = exerciseManager.updateExercises().subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                updateAdapter();
            }
        });
    }


    @Override
    public void updateAdapter() {
        adapterData = new ArrayList<>();

        List<Exercise> exercises;

        if (showAll && favOnly)
            exercises = exerciseRepo.getAllFavouriteExercises();
        else if(showAll)
            exercises = exerciseRepo.getAllExercises();
        else
            exercises = exerciseRepo.getExercises(categoryId, false);


        if (exercises != null)
            adapterData.addAll(exercises);


        ArrayList<ExerciseListModel> items = new ArrayList<>();
        for (Exercise exercise : exercises) {
            items.add(new ExerciseListModel(exercise));
        }
        view.updateAdapter(items);
    }



    // Contracts
    //
    @Override
    public void viewCreated(int categoryId, boolean favOnly, boolean showAll, boolean isAddSet) {
        this.categoryId = categoryId;
        this.isAddSet = isAddSet;
        this.favOnly = favOnly;
        this.showAll = showAll;
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
    public void deleteExercise(int position) {
        exerciseRepo.deleteExercise(adapterData.get(position).getId());
        adapterData.remove(position);
        view.removeItem(position);
        updateAdapter();
        exerciseManager.exerciseUpdated(true);
    }

    @Override
    public Exercise getExercise(int position) {
        return adapterData.get(position);
    }

    @Override
    public int getCategoryId() {
        return categoryId;
    }

    @Override
    public boolean isShowAll() {
        return showAll;
    }

    @Override
    public boolean isFavOnly() {
        return favOnly;
    }
}
