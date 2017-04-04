package com.brandonhogan.liftscout.presenters;

import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.TodayContract;
import com.brandonhogan.liftscout.managers.ProgressManager;
import com.brandonhogan.liftscout.managers.RecordsManager;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.models.TodayListItemModel;
import com.brandonhogan.liftscout.models.TodayListSectionModel;
import com.brandonhogan.liftscout.repository.model.Rep;
import com.brandonhogan.liftscout.repository.model.Set;
import com.brandonhogan.liftscout.utils.DateUtil;
import com.brandonhogan.liftscout.utils.Constants;
import com.mikepenz.fastadapter.IItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.realm.RealmList;

public class TodayPresenter implements TodayContract.Presenter {

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
    private TodayContract.View view;
    private Date date;
    private ArrayList<TodayListSectionModel> adapterData;


    // Constructor
    //
    public TodayPresenter(TodayContract.View view, long dateLong) {
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
            int setCounter = 0;
            for (Set set : sets.sort(Set.ORDER_ID)) {

                List<IItem> items = new LinkedList<>();
                double volume = 0;
                boolean isEmpty = true;
                int setCount = 0;

                int repCounter = 0;
                for (Rep rep : set.getReps()) {

                    items.add(new TodayListItemModel(set.getId(), set.getExercise().getId(), rep.getCount(), rep.getWeight(), userManager.getMeasurementValue(),
                            recordsManager.isRecord(rep.getId()),set.getReps().size() - 1 == repCounter));
                    volume += (rep.getWeight() * rep.getCount());
                    isEmpty = false;
                    repCounter ++;
                }

                if (isEmpty)
                    items.add(new TodayListItemModel(set.getId(), set.getExercise().getId(), true, view.getEmptySetMessage(), true));
                else
                    setCount = set.getReps().size();


                TodayListSectionModel expandableItem = new TodayListSectionModel(set.getId(), set.getExercise().getName(), volume, setCount, userManager.getMeasurementValue(), isEmpty, setCounter == 0);
                expandableItem.withSubItems(items);
                adapterData.add(expandableItem);
                setCounter ++;
            }
        }

        // Checks the manager to see if a set has been updated.
        // If so, it will check the current sets to see if it matches, and updates it
        Set updatedSet = progressManager.getUpdatedSet();
        if (updatedSet != null) {
            int pos = 0;
            for (TodayListSectionModel section : adapterData) {
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

        int dateRes = DateUtil.toRelativeDateRes(date);

        if (dateRes != 0) {
            view.setupTitle(dateRes, null);
        }
        else {
            view.setupTitle(0, new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT, Locale.getDefault()).format(date));
        }
    }

    @Override
    public void onDeleteSection(TodayListSectionModel section, int position) {
        adapterData.remove(adapterData.indexOf(section));

        //The adapter will remove a range. Which is the children, plus the section
        int count = section.getSubItems().size() + 1;

        progressManager.deleteSet(section.setId);
        progressManager.clearUpdatedSet();
        view.onSetDeleted(position, count);
    }
}
