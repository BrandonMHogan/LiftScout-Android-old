package com.brandonhogan.liftscout.views.workout.tracker;

import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.injection.components.Injector;

import java.util.ArrayList;

import javax.inject.Inject;

public class TrackerPresenter implements TrackerContract.Presenter {


    // Injections
    //
    @Inject
    ProgressManager progressManager;

    // Private Properties
    //
    private TrackerContract.View view;
    private int exerciseId;
    private Set set;
    private ArrayList<TrackerListModel> adapterData;
    private int selectedPosition;
    private TrackerListModel editingRep;



    // Constructor
    //
    public TrackerPresenter(TrackerContract.View view, int exerciseId) {
        Injector.getAppComponent().inject(this);

        this.view = view;
        this.exerciseId = exerciseId;

    }


    // Private Functions
    //
    private void resetAdapter() {
        adapterData = null;
        updateAdapter();
    }


    // Contract
    //
    @Override
    public void viewCreated() {
        updateAdapter();
    }

    @Override
    public void updateAdapter() {
        if (adapterData != null)
            view.updateAdapter(adapterData);

        adapterData = new ArrayList<>();

        set = progressManager.getTodayProgressSet(exerciseId);

        if (set != null) {
            int rowNum = 1;
            for (Rep rep : set.getReps()) {
                adapterData.add(new TrackerListModel(
                        rowNum,
                        rep,
                        view.getRepsLabel(rep.getCount() > 1),
                        "lbs"));
                rowNum += 1;
            }
        }

        view.updateAdapter(adapterData);
    }

    @Override
    public void onSave(String repValue, String weightValue) {

        Rep rep = new Rep();

        int count = Integer.valueOf(repValue);
        rep.setCount(count);

        double weight = Double.valueOf(weightValue);
        rep.setWeight(weight);

        if(editingRep != null) {
            rep.setId(editingRep.getId());
            progressManager.updateRep(rep);
        }
        else {
            progressManager.addRepToTodayProgress(set, rep);
        }

        progressManager.updateSet(set);
        resetAdapter();

        view.saveSuccess(editingRep == null ? adapterData.size() - 1 : selectedPosition);
        editingRep = null;
    }

    /*
        Called when the delete button is pressed in the view.
        If a set is empty, then delete the set
        If a set is not empty, show the delete set alert
        If a rep is selected, show the delete rep alert
     */
    @Override
    public void onDelete() {

        if (editingRep == null) {
            if (set.getReps().isEmpty())
                onDeleteSet();
            else
                view.showDeleteSetAlert();
        }
        else {
            view.showDeleteRepAlert();
        }
    }

    @Override
    public void onDeleteSet() {
        progressManager.deleteSet(set);
        progressManager.clearUpdatedSet();
        view.deleteSetSuccess();
    }

    @Override
    public void onDeleteRep() {
        if (editingRep != null) {
            progressManager.deleteRep(editingRep.getId());
            editingRep = null;
            resetAdapter();
            view.clear(false);
        }
    }

    @Override
    public String getExerciseName() {
        return set.getExercise().getName();
    }

    @Override
    public void onSelect(int position) {
        selectedPosition = position;
        editingRep = adapterData.get(position);
        view.onSelect(editingRep);
    }

    @Override
    public void onButtonTwoPressed() {

        if (editingRep == null) {
            view.clear(true);
        }
        else {
            editingRep = null;
            view.clear(false);
        }
    }
}
