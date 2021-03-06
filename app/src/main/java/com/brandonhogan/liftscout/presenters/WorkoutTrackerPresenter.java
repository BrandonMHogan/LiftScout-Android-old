package com.brandonhogan.liftscout.presenters;

import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.WorkoutTrackerContract;
import com.brandonhogan.liftscout.managers.ProgressManager;
import com.brandonhogan.liftscout.managers.RecordsManager;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.models.TrackerListModel;
import com.brandonhogan.liftscout.repository.ExerciseRepo;
import com.brandonhogan.liftscout.repository.impl.ExerciseRepoImpl;
import com.brandonhogan.liftscout.repository.model.Rep;
import com.brandonhogan.liftscout.repository.model.Set;
import com.brandonhogan.liftscout.utils.Constants;
import com.brandonhogan.liftscout.utils.DateUtil;
import com.brandonhogan.liftscout.utils.constants.ConstantValues;
import com.brandonhogan.liftscout.utils.constants.Measurements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

public class WorkoutTrackerPresenter implements WorkoutTrackerContract.Presenter {

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
    private WorkoutTrackerContract.View view;
    private int exerciseId;
    private Set set;
    private ArrayList<TrackerListModel> adapterData;
    private int selectedPosition;
    private TrackerListModel editingRep;
    private String measurementType;

    private ExerciseRepo exerciseRepo;


    // Constructor
    //
    public WorkoutTrackerPresenter(WorkoutTrackerContract.View view, int exerciseId) {
        Injector.getAppComponent().inject(this);

        this.view = view;
        this.exerciseId = exerciseId;
        measurementType = userManager.getMeasurementValue();

        exerciseRepo = new ExerciseRepoImpl();
    }


    // Private Functions
    //
    private void resetAdapter() {
        adapterData = null;
        updateAdapter();
    }

    private double getDefaultIncrement() {
        if (userManager.getMeasurementValue().equals(Measurements.KILOGRAMS))
            return ConstantValues.INCREMENT_KG_DEFAULT;
        else
            return ConstantValues.INCREMENT_LB_DEFAULT;
    }


    // Contract
    //
    @Override
    public void viewCreated() {
        updateAdapter();

        int dateRes = DateUtil.toRelativeDateRes(progressManager.getTodayProgress().getDate());

        if (dateRes != 0) {
            view.setDate(dateRes, null);
        }
        else {
            view.setDate(0, new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT, Locale.getDefault()).format(progressManager.getTodayProgress().getDate()));
        }

        updateIncrement();
    }

    @Override
    public void updateAdapter() {
        if (adapterData != null) {
            view.updateAdapter(adapterData);
        }

            adapterData = new ArrayList<>();

            set = progressManager.getTodayProgressSet(exerciseId);

            if (set != null) {
                int rowNum = 1;
                for (Rep rep : set.getReps()) {

                    boolean isRecord = recordsManager.isRecord(rep.getId());

                    adapterData.add(new TrackerListModel(
                            rowNum,
                            rep,
                            view.getRepsLabel(rep.getCount() > 1),
                            Measurements.getCompressedType(measurementType, rep.getWeight() > 1), isRecord
                            , set.getReps().size() == rowNum));
                    rowNum += 1;
                }
            }

            view.updateAdapter(adapterData);


        if (adapterData != null && adapterData.size() > 0) {
            TrackerListModel model = adapterData.get(adapterData.size() -1);
            view.updateValues((float)model.getWeight(), model.getCount());
        }
        else {
            // Need to check to see what weight/rep was used last time this exercise was done
            Set set = progressManager.getPreviousSet(exerciseId);

            if(set != null && set.getReps() != null && !set.getReps().isEmpty()) {
                Rep rep = set.getReps().get(0);

                view.updateValues((float)rep.getWeight(), rep.getCount());
            }

        }
    }

    @Override
    public void updateIncrement() {
        double increment = exerciseRepo.getExerciseIncrement(exerciseId);

        if(increment == 0)
            increment = getDefaultIncrement();

        view.updateIncrements(increment);
    }

    @Override
    public void onSave(String repValue, String weightValue) {

        Rep rep = new Rep();

        int count = Integer.valueOf(repValue);
        rep.setCount(count);

        double weight = Double.valueOf(weightValue);
        rep.setWeight(weight);

        // If its an edit or create
        if(editingRep != null) {
            rep.setId(editingRep.getId());
            progressManager.updateRep(rep, exerciseId);
        }
        else {
            progressManager.addRepToTodayProgress(set, rep);
        }

        progressManager.updateSet(set);
        resetAdapter();

        if (editingRep == null)
            view.saveSuccess(adapterData.size() - 1, true);
        else
            view.saveSuccess(selectedPosition, false);

        editingRep = null;
    }

    @Override
    public void onDeleteRep(int position) {
        if (adapterData != null) {
            editingRep = adapterData.get(position);
            progressManager.deleteRep(editingRep.getId(), exerciseId);
            editingRep = null;
            resetAdapter();
            view.clear(false, null);
            view.onDelete(position);
        }
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
            view.clear(true, null);
        }
        else {
            editingRep = null;
            TrackerListModel model = adapterData.get(adapterData.size() - 1);
            view.clear(false, model);
        }
    }
}
