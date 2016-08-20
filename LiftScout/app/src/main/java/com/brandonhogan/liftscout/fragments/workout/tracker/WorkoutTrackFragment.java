package com.brandonhogan.liftscout.fragments.workout.tracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.constants.Bundles;
import com.brandonhogan.liftscout.core.controls.NumberPicker;
import com.brandonhogan.liftscout.core.model.Exercise;
import com.brandonhogan.liftscout.core.model.Progress;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.fragments.base.BaseFragment;
import com.nikhilpanju.recyclerviewenhanced.OnActivityTouchListener;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.RealmList;

public class WorkoutTrackFragment extends BaseFragment implements RecyclerTouchListener.RecyclerTouchListenerHelper {

    // Static Properties
    //
    private static final String BUNDLE_EXERCISE_ID = "exerciseIdBundle";
    private static final String BUNDLE_PROGRESS_ID = "progressIdBundle";


    // Instance
    //
    public static WorkoutTrackFragment newInstance(long progressId, int exerciseId)
    {
        WorkoutTrackFragment frag = new WorkoutTrackFragment();
        Bundle bundle = new Bundle();

        bundle.putLong(BUNDLE_PROGRESS_ID, progressId);
        bundle.putInt(BUNDLE_EXERCISE_ID, exerciseId);
        frag.setArguments(bundle);

        return frag;
    }


    // Private Properties
    //
    private View rootView;
    private int exerciseId;
    private long progressId;
    private boolean isNewSet;

    private WorkoutTrackerAdapter mAdapter;
    private RecyclerTouchListener onTouchListener;
    private OnActivityTouchListener touchListener;
    private SweetAlertDialog dialog;

    private List<TrackerListModel> _workout;
    private Set _set;
    private Rep selectedRep;
    private Progress progress;

    // Binds
    //
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.first_button)
    Button firstButton;

    @Bind(R.id.second_button)
    Button secondButton;

    @Bind(R.id.rep_number_picker)
    NumberPicker repNumberPicker;

    @Bind(R.id.weight_number_picker)
    NumberPicker weightNumberPicker;

    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_workout_track, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        exerciseId = getArguments().getInt(BUNDLE_EXERCISE_ID, Bundles.SHIT_ID);
        progressId = getArguments().getLong(BUNDLE_PROGRESS_ID, Bundles.SHIT_ID);

        setupButtons();
        setupAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView.addOnItemTouchListener(onTouchListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mRecyclerView.removeOnItemTouchListener(onTouchListener);
    }

    @Override
    public void setOnActivityTouchListener(OnActivityTouchListener listener) {
        this.touchListener = listener;
    }

    private void setupButtons() {
        firstButton.setText(getString(R.string.save));
        secondButton.setText(getString(R.string.clear));
    }

    private void setupAdapter() {
        mAdapter = new WorkoutTrackerAdapter(getActivity(), getData());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        onTouchListener = new RecyclerTouchListener(getActivity(), mRecyclerView);

        onTouchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {
                    }
                })
                .setSwipeOptionViews(R.id.delete, R.id.edit)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {

                        if (viewID == R.id.delete) {
                            removeRepAlert(position);
                        } else if (viewID == R.id.edit) {
                            editRep(position);
                        }
                    }
                });
    }

    private List<TrackerListModel> getData() {

        if (_workout != null)
            return _workout;

        _workout = new ArrayList<>();

        int rowNum = 1;
        for (Rep rep : getRep()) {
            _workout.add(new TrackerListModel(
                    rowNum,
                    rep,
                    getResources().getString(R.string.reps),
                    getResources().getString(R.string.lbs)));
            rowNum +=1;
        }

        return _workout;
    }

    private void update() {
        _workout = null;
        _set = null;
        mAdapter.setList(getData());
    }

    private Set getSet() {

        if (_set != null)
            return _set;

        progress = getRealm().where(Progress.class).equalTo(Progress.ID, progressId).findFirst();
        _set = progress.getSets().where().equalTo("exercise.id", exerciseId).findFirst();

        if (_set == null) {
            isNewSet = true;

            _set = new Set();
            _set.setExercise(getRealm().where(Exercise.class).equalTo(Exercise.ID, exerciseId).findFirst());
            _set.setReps(new RealmList<Rep>());
        }

        return _set;
    }

    private RealmList<Rep> getRep() {
        return getSet().getReps();
    }

    private void saveRep() {
        Rep rep = (selectedRep == null ? new Rep() : selectedRep);


        int count = Integer.valueOf(repNumberPicker.getNumber());
        rep.setCount(count);

        double weight = Double.valueOf(weightNumberPicker.getNumber());
        rep.setWeight(weight);


        getRealm().beginTransaction();

        if (isNewSet) {
            progress = getRealm().where(Progress.class).equalTo(Progress.ID, progressId).findFirst();
            progress.getSets().add(_set);
            isNewSet = false;
        }

        _set.getReps().add(rep);
        getRealm().copyToRealmOrUpdate(progress);
        getRealm().copyToRealmOrUpdate(_set);
        getRealm().commitTransaction();

        selectedRep = null;

        update();
    }

    private void removeRep(int id) {
        getRealm().beginTransaction();
        getRealm().where(Rep.class).equalTo(Rep.ID, id).findFirst().deleteFromRealm();
        getRealm().commitTransaction();

        update();
    }

    private void editRep(int position) {
        Log.e(getTAG(), "Still need to implement Editing Reps");
        selectedRep = getSet().getReps().where().equalTo(Rep.ID, getData().get(position).getId()).findFirst();


    }

    private void removeRepAlert(final int position) {
        dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.dialog_rep_remove_title))
                .setContentText(getString(R.string.dialog_rep_remove_message))
                .setConfirmText(getString(R.string.yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        removeRep(getData().get(position).getId());
                        dialog.cancel();
                    }
                })
                .setCancelText(getString(R.string.cancel))
                .showCancelButton(true);

        dialog.show();
    }

    @OnClick(R.id.first_button)
    public void firstButtonOnClick() {
        saveRep();
    }

    @OnClick(R.id.second_button)
    public void secondButtonOnClick() {
        repNumberPicker.setNumber(0);
        weightNumberPicker.setNumber(0);
    }
}
