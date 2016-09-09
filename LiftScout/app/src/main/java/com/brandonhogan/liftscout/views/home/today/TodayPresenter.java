package com.brandonhogan.liftscout.views.home.today;

import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.core.utils.Constants;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.mikepenz.fastadapter.IItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.realm.RealmList;

public class TodayPresenter implements TodayContact.Presenter {

    // Injections
    //
    @Inject
    ProgressManager progressManager;

    // Private Properties
    //
    private TodayContact.View view;
    private Date date;
    private String dateString;
    private String dateYear;
    private ArrayList<TodayListSection> adapterData;


    // Constructor
    //
    public TodayPresenter(TodayContact.View view, long dateLong) {
        Injector.getAppComponent().inject(this);
        this.view = view;
        this.date = new Date(dateLong);

        dateString = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT, Locale.getDefault()).format(date);
        dateYear = new SimpleDateFormat(Constants.SIMPLE_DATE_YEAR_FORMAT, Locale.getDefault()).format(date);
    }


    // Private Functions
    //
    private void updateAdapter() {

        adapterData = new ArrayList<>();
        RealmList<Set> sets = progressManager.getSetsByDate(date);

        if (sets != null) {
            for (Set set : sets.sort(Set.ORDER_ID)) {

                List<IItem> items = new LinkedList<>();
                double volume = 0;
                boolean isEmpty = true;

                for (Rep rep : set.getReps()) {
                    items.add(new TodayListItem(set.getId(), set.getExercise().getId(), rep.getCount(), rep.getWeight()));
                    volume += rep.getWeight();
                    isEmpty = false;
                }

                if (isEmpty)
                    items.add(new TodayListItem(set.getId(), set.getExercise().getId(), true, view.getEmptySetMessage()));

                TodayListSection expandableItem = new TodayListSection(set.getId(), set.getExercise().getName(), volume, isEmpty);
                expandableItem.withSubItems(items);
                adapterData.add(expandableItem);
            }
        }

        // Checks the manager to see if a set has been updated.
        // If so, it will check the current sets to see if it matches, and updates it
        Set updatedSet = progressManager.getUpdatedSet();
        if (updatedSet != null) {
            int pos = 0;
            for (TodayListSection section : adapterData) {
                if (section.setId == updatedSet.getId()) {
                    view.setupAdapter(adapterData, pos);
                    progressManager.clearUpdatedSet();
                    return;
                }
                pos +=1;
            }
        }

        view.setupAdapter(adapterData, 0);
    }

    @Override
    public void viewCreate() {
        update();
    }

    @Override
    public void update() {
        updateAdapter();

        double weight = progressManager.getTodayProgress().getWeight();

        view.setupTitle(dateString, dateYear);
        view.setupWeight(weight == 0 ? null : Double.toString(weight));
    }

    @Override
    public void itemTouchOnMove(int oldId, int newId) {
        progressManager.swapSetOrders(oldId, newId);
    }
}
