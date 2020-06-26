package com.brandonhogan.liftscout.presenters;

import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.GraphsCategoriesContract;
import com.brandonhogan.liftscout.managers.ProgressManager;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.repository.CategoryRepo;
import com.brandonhogan.liftscout.repository.impl.CategoryRepoImpl;
import com.brandonhogan.liftscout.repository.model.Category;
import com.brandonhogan.liftscout.repository.model.CategoryGraph;
import com.brandonhogan.liftscout.repository.model.Rep;
import com.brandonhogan.liftscout.repository.model.Set;
import com.brandonhogan.liftscout.utils.constants.Charts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    private List<Category> categories;
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
        int total = 0;
        long start = 0, end = 0;

        for (final Category category : categories) {
            int count = 0;

            switch (graphTypes.get(currentGraphTypePosition)) {
                case Charts.CATEOGRY_BREAKDOWN_TOTAL_WORKOUT:
                    count = (int)sets.where().equalTo("exercise.categoryId", category.getId()).distinct(Set.DATE).count();
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
            if (count > 0) {
                categoryGraphs.add(new CategoryGraph(category.getName(), count, category.getColor()));
                total += count;
            }

            Collections.sort(categoryGraphs, new Comparator<CategoryGraph>() {
                @Override
                public int compare(CategoryGraph categoryGraph, CategoryGraph t1) {
                    return categoryGraph.getValue() > t1.getValue() ? -1 : (categoryGraph.getValue() < t1.getValue()) ? 1 : 0;
                }
            });
        }

        if (sets != null && sets.size() > 0) {
            start = sets.where().minimumDate(Set.DATE).getTime();
            end = sets.where().maximumDate(Set.DATE).getTime();
        }

        view.setPieChart(categoryGraphs, total, start, end);
    }

    @Override
    public void onGraphTypeSelected(int position) {
        currentGraphTypePosition = position;
        setupPieChart();
    }
}
