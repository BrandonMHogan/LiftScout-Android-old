package com.brandonhogan.liftscout.views.workout.tracker;

import android.util.Log;

import com.brandonhogan.liftscout.core.constants.Measurements;
import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.core.managers.RecordsManager;
import com.brandonhogan.liftscout.core.managers.UserManager;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.ExerciseRepo;
import com.brandonhogan.liftscout.repository.impl.ExerciseRepoImpl;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class TrackerPresenter implements TrackerContract.Presenter {

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
    private TrackerContract.View view;
    private int exerciseId;
    private Set set;
    private ArrayList<TrackerListModel> adapterData;
    private int selectedPosition;
    private TrackerListModel editingRep;
    private String measurementType;

    private ExerciseRepo exerciseRepo;


    // Constructor
    //
    public TrackerPresenter(TrackerContract.View view, int exerciseId) {
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



    // Contract
    //
    @Override
    public void viewCreated() {
        updateAdapter();
        view.setDate(progressManager.getTodayProgress().getDate());
        updateIncrement();
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

                boolean isRecord = recordsManager.isRecord(rep.getId());

                adapterData.add(new TrackerListModel(
                        rowNum,
                        rep,
                        view.getRepsLabel(rep.getCount() > 1),
                        Measurements.getCompressedType(measurementType, rep.getWeight() > 1), isRecord));
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
        view.updateIncrements(exerciseRepo.getExerciseIncrement(exerciseId));
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
            progressManager.updateRep(rep);

            recordsManager.repUpdated(rep.getId(), exerciseId)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(@NonNull Boolean aBoolean) throws Exception {
                            Log.d("", "accept: ");
                        }
                    });
        }
        else {
            progressManager.addRepToTodayProgress(set, rep);

            //If create, then we need to check the recordsManager
            recordsManager.repCreated(rep.getId(), exerciseId)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(@NonNull Boolean aBoolean) throws Exception {
                            Log.d("", "accept: ");
                        }
                    });

        }

        progressManager.updateSet(set);
        resetAdapter();

        view.saveSuccess(editingRep == null ? adapterData.size() - 1 : selectedPosition);
        editingRep = null;
    }

    @Override
    public void onDeleteRep() {
        if (editingRep != null) {

            //Deletes the rep from records
            recordsManager.repDeleted(editingRep.getId())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {

                        // Updates the rest of the reps in that rep range
                        recordsManager.updateRepRange(exerciseId, editingRep.getCount())
                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                                        progressManager.deleteRep(editingRep.getId());
                                        editingRep = null;
                                        resetAdapter();
                                        view.clear(false);
                                    }
                                });
                    }
                });
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
            view.clear(true);
        }
        else {
            editingRep = null;
            view.clear(false);
        }
    }
}
