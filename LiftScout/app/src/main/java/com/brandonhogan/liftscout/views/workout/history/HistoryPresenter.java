package com.brandonhogan.liftscout.views.workout.history;

import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.mikepenz.fastadapter.IItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmResults;

public class HistoryPresenter implements HistoryContract.Presenter {


    // Injections
    //
    @Inject
    ProgressManager progressManager;

    // Private Properties
    //
    private HistoryContract.View view;
    private int exerciseId;
    private ArrayList<HistoryListSection> adapterData;

    // Constructor
    //
    public HistoryPresenter(HistoryContract.View view, int exerciseId) {
        Injector.getAppComponent().inject(this);

        this.view = view;
        this.exerciseId = exerciseId;
    }


    // Private Functions
    //
    private void updateAdapter() {

        adapterData = new ArrayList<>();
        RealmResults<Set> sets = progressManager.getSetsByExercise(exerciseId);

        if (sets != null) {
            for (Set set : sets) {

                List<IItem> items = new LinkedList<>();
                double volume = 0;
                boolean isEmpty = true;

                for (Rep rep : set.getReps()) {
                    items.add(new HistoryListItem(set.getId(), set.getExercise().getId(), rep.getCount(), rep.getWeight()));
                    volume += rep.getWeight();
                    isEmpty = false;
                }

                if (isEmpty)
                    items.add(new HistoryListItem(set.getId(), set.getExercise().getId(), true, view.getEmptySetMessage()));

                HistoryListSection expandableItem = new HistoryListSection(set.getId(), set.getDate(), volume, isEmpty);
                expandableItem.withIsExpanded(true);
                expandableItem.withSubItems(items);
                adapterData.add(expandableItem);
            }
        }

        view.setupAdapter(adapterData);
    }


    //Contracts
    //


    @Override
    public void viewCreated() {
        updateAdapter();
    }

    @Override
    public void update() {
        updateAdapter();
    }
}
