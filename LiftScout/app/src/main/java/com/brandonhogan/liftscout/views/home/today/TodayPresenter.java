package com.brandonhogan.liftscout.views.home.today;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.core.managers.UserManager;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.core.utils.BhDate;
import com.brandonhogan.liftscout.core.utils.Constants;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.mikepenz.fastadapter.IItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    @Inject
    UserManager userManager;

    // Private Properties
    //
    private TodayContact.View view;
    private Date date;
    private ArrayList<TodayListSection> adapterData;


    // Constructor
    //
    public TodayPresenter(TodayContact.View view, long dateLong) {
        Injector.getAppComponent().inject(this);
        this.view = view;
        this.date = new Date(dateLong);
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
                int setCount = 0;

                for (Rep rep : set.getReps()) {
                    items.add(new TodayListItem(set.getId(), set.getExercise().getId(), rep.getCount(), rep.getWeight(), userManager.getMeasurementValue()));
                    volume += (rep.getWeight() * rep.getCount());
                    isEmpty = false;
                }

                if (isEmpty)
                    items.add(new TodayListItem(set.getId(), set.getExercise().getId(), true, view.getEmptySetMessage()));
                else
                    setCount = set.getReps().size();


                TodayListSection expandableItem = new TodayListSection(set.getId(), set.getExercise().getName(), volume, setCount, userManager.getMeasurementValue(), isEmpty);
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
        view.setupWeight(weight == 0 ? null : Double.toString(weight));


        Calendar cal = Calendar.getInstance();
        cal.setTime(BhDate.trimTimeFromDate(new Date()));

        if (date.equals(cal.getTime())) {
            view.setupTitle(R.string.today, null);
            return;
        }

        cal.add(Calendar.DATE, -1);
        if (date.equals(cal.getTime())) {
            view.setupTitle(R.string.yesterday, null);
            return;
        }

        cal.add(Calendar.DATE, 2);
        if (date.equals(cal.getTime())) {
            view.setupTitle(R.string.tomorrow, null);
            return;
        }

        view.setupTitle(0, new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT, Locale.getDefault()).format(date));
    }

    @Override
    public void onDeleteSection(TodayListSection section, int position) {
        adapterData.remove(adapterData.indexOf(section));

        //The adapter will remove a range. Which is the children, plus the section
        int count = section.getSubItems().size() + 1;

        progressManager.deleteSet(section.setId);
        progressManager.clearUpdatedSet();
        view.onSetDeleted(position, count);
    }
}
