package com.brandonhogan.liftscout.views.graphs.categories;

import com.brandonhogan.liftscout.core.constants.Charts;
import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.core.managers.UserManager;
import com.brandonhogan.liftscout.core.model.Category;
import com.brandonhogan.liftscout.core.model.CategoryGraph;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.CategoryRepo;
import com.brandonhogan.liftscout.repository.impl.CategoryRepoImpl;

import java.util.ArrayList;

import javax.inject.Inject;

import io.realm.RealmResults;

/**
 * Created by Brandon on 2/27/2017.
 * Description :
 */

public class GraphsCategoriesPresenter implements GraphsCategoriesContract.Presenter {

    // Injections
    //
    @Inject
    ProgressManager progressManager;

    @Inject
    UserManager userManager;

    // Private Properties
    //
    private GraphsCategoriesContract.View view;
    private CategoryRepo categoryRepo;
    private RealmResults<Category> categories;
    private ArrayList<Integer> graphTypes;
    private int currentGraphTypePosition;

    // Constructor
    //
    public GraphsCategoriesPresenter(GraphsCategoriesContract.View view) {
        Injector.getAppComponent().inject(this);

        this.view = view;
        categoryRepo = new CategoryRepoImpl();
    }

    @Override
    public void viewCreated() {

        categories = categoryRepo.getCategories();

        setupGraphTypes();
        setupPieChart();
    }


    private void setupGraphTypes() {
        graphTypes = new ArrayList<>();
        graphTypes.add(Charts.CATEOGRY_BREAKDOWN_TOTAL_WORKOUT);
        graphTypes.add(Charts.CATEOGRY_BREAKDOWN_TOTAL_SETS);
        graphTypes.add(Charts.CATEOGRY_BREAKDOWN_TOTAL_REPS);
        view.populateGraphType(graphTypes, currentGraphTypePosition = 0);
    }

    /*
        Pie chart shows total workouts for each category.
     */
    private void setupPieChart() {
        RealmResults<Set> sets = progressManager.getAllSets();
        ArrayList<CategoryGraph> categoryGraphs = new ArrayList<>();

        for (Category category : categories) {
            int count = 0;

            switch (graphTypes.get(currentGraphTypePosition)) {
                case Charts.CATEOGRY_BREAKDOWN_TOTAL_WORKOUT:
                    count = sets.where().equalTo("exercise.categoryId", category.getId()).distinct(Set.DATE).size();
                    break;
                case Charts.CATEOGRY_BREAKDOWN_TOTAL_SETS:
                    count = sets.where().equalTo("exercise.categoryId", category.getId()).findAll().size();
                    break;
                case Charts.CATEOGRY_BREAKDOWN_TOTAL_REPS:
                    RealmResults<Set> items = sets.where().equalTo("exercise.categoryId", category.getId()).findAll();

                    for (Set item : items) {
                        for (Rep rep : item.getReps()) {
                            count += rep.getCount();
                        }
                    }
                    break;
            }

            // Only add categories that have at least one entry
            if (count > 0)
                categoryGraphs.add(new CategoryGraph(category.getName(), count, category.getColor()));
        }

        view.setPieChart(categoryGraphs, graphTypes.get(currentGraphTypePosition));
    }

    @Override
    public void onGraphTypeSelected(int position) {
        currentGraphTypePosition = position;
        setupPieChart();
    }
}
