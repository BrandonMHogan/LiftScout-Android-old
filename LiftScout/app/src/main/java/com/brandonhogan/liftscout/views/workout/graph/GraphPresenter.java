package com.brandonhogan.liftscout.views.workout.graph;

import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.views.workout.history.HistoryListItem;
import com.brandonhogan.liftscout.views.workout.history.HistoryListSection;
import com.mikepenz.fastadapter.IItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmResults;

/**
 * Created by Brandon on 2/15/2017.
 * Description :
 */

public class GraphPresenter implements GraphContract.Presenter {

    // Injections
    //
    @Inject
    ProgressManager progressManager;

    // Private Properties
    //
    private GraphContract.View view;
    private int exerciseId;

    // Constructor
    //
    public GraphPresenter(GraphContract.View view, int exerciseId) {
        Injector.getAppComponent().inject(this);

        this.view = view;
        this.exerciseId = exerciseId;
    }

    @Override
    public void viewCreated() {
        update();
    }

    @Override
    public void update() {
        RealmResults<Set> sets = progressManager.getSetsByExercise(exerciseId);
        List<GraphDataSet> items = new LinkedList<>();

        if (sets != null) {
            for (int count = sets.size() -1; count >= 0; count--) {

                double volume = 0;

                for (Rep rep : sets.get(count).getReps()) {
                    volume += rep.getWeight() * rep.getCount();
                }

                if (volume > 0){
                    GraphDataSet item = new GraphDataSet(count, volume);
                    items.add(item);
                }

            }
        }
        view.setGraph(items);
    }
}
