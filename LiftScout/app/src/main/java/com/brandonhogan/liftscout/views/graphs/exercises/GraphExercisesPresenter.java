package com.brandonhogan.liftscout.views.graphs.exercises;

import com.brandonhogan.liftscout.core.managers.GraphManager;
import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.core.managers.UserManager;
import com.brandonhogan.liftscout.core.model.Category;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.CategoryRepo;
import com.brandonhogan.liftscout.repository.ExerciseRepo;
import com.brandonhogan.liftscout.repository.impl.CategoryRepoImpl;
import com.brandonhogan.liftscout.repository.impl.ExerciseRepoImpl;
import com.brandonhogan.liftscout.views.graphs.categories.GraphsCategoriesContract;

import java.util.ArrayList;

import javax.inject.Inject;

import io.realm.RealmResults;

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
            currentExerciseId = graphManager.getCurrentExerciseId();
            graphManager.setInSearch(false);

            view.setSelectedExercise(currentExerciseId, exerciseRepo.getExercise(currentExerciseId).getName());
        }
    }
}
