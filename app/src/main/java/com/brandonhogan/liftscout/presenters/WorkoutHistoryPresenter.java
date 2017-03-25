package com.brandonhogan.liftscout.presenters;

import com.brandonhogan.liftscout.managers.ProgressManager;
import com.brandonhogan.liftscout.managers.RecordsManager;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.repository.model.Rep;
import com.brandonhogan.liftscout.repository.model.Set;
import com.brandonhogan.liftscout.utils.BhDate;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.HistoryContract;
import com.brandonhogan.liftscout.models.HistoryListItemModel;
import com.brandonhogan.liftscout.models.HistoryListSectionModel;
import com.mikepenz.fastadapter.IItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmResults;

public class WorkoutHistoryPresenter implements HistoryContract.Presenter {


    // Injections
    //
    @Inject
    ProgressManager progressManager;

    @Inject
    UserManager userManager;

    @Inject
    RecordsManager recordsManager;

    // Private Properties
    //
    private HistoryContract.View view;
    private int exerciseId;
    private ArrayList<HistoryListSectionModel> adapterData;

    // Constructor
    //
    public WorkoutHistoryPresenter(HistoryContract.View view, int exerciseId) {
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
            int setCounter = 0;
            for (Set set : sets) {

                List<IItem> items = new LinkedList<>();
                double volume = 0;
                boolean isEmpty = true;
                int setCount = 0;

                int repCounter = 0;
                for (Rep rep : set.getReps()) {
                    items.add(new HistoryListItemModel(set.getId(), set.getExercise().getId(), set.getDate(), rep.getCount(), rep.getWeight(), userManager.getMeasurementValue(),
                            recordsManager.isRecord(rep.getId()), set.getReps().size() - 1 == repCounter));
                    volume += rep.getWeight();
                    isEmpty = false;
                    repCounter ++;
                }

                if (isEmpty)
                    items.add(new HistoryListItemModel(set.getId(), set.getExercise().getId(), set.getDate(), true, view.getEmptySetMessage()));
                else
                    setCount = set.getReps().size();

                HistoryListSectionModel expandableItem = new HistoryListSectionModel(set.getId(), set.getDate(), BhDate.toSimpleStringDate(set.getDate()), set.getExercise().getId(), volume, setCount, userManager.getMeasurementValue(), isEmpty, setCounter == 0);
                expandableItem.withIsExpanded(true);
                expandableItem.withSubItems(items);
                adapterData.add(expandableItem);
                setCounter ++;
            }
        }

        if (sets != null && sets.size() > 0)
            view.setupTitle(sets.where().minimumDate(Set.DATE).getTime(), sets.where().maximumDate(Set.DATE).getTime());

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

    @Override
    public void setClicked(int exerciseId, Date date) {
        progressManager.setTodayProgress(date);
        view.editTracker(exerciseId);
    }
}
