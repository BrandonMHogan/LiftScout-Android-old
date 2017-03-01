package com.brandonhogan.liftscout.views.graphs.exercises;

import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.core.managers.UserManager;
import com.brandonhogan.liftscout.core.model.Category;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.CategoryRepo;
import com.brandonhogan.liftscout.repository.impl.CategoryRepoImpl;
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
    UserManager userManager;

    // Private Properties
    //
    private GraphExercisesContract.View view;
    private CategoryRepo categoryRepo;
    private RealmResults<Category> categories;
    private ArrayList<Integer> graphTypes;
    private int currentGraphTypePosition;

    // Constructor
    //
    public GraphExercisesPresenter(GraphExercisesContract.View view) {
        Injector.getAppComponent().inject(this);

        this.view = view;
        categoryRepo = new CategoryRepoImpl();
    }

    @Override
    public void viewCreated() {

    }
}
