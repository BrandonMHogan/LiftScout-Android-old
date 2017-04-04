package com.brandonhogan.liftscout.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.adapters.TrackerAdapter;
import com.brandonhogan.liftscout.events.IncrementEvent;
import com.brandonhogan.liftscout.events.TrackerEvent;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.RecyclerViewClickListener;
import com.brandonhogan.liftscout.interfaces.contracts.WorkoutTrackerContract;
import com.brandonhogan.liftscout.models.TrackerListModel;
import com.brandonhogan.liftscout.presenters.WorkoutTrackerPresenter;
import com.brandonhogan.liftscout.utils.constants.Bundles;
import com.brandonhogan.liftscout.utils.controls.NumberPicker;
import com.brandonhogan.liftscout.views.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class WorkoutTrackerFragment extends BaseFragment implements
        WorkoutTrackerContract.View, RecyclerViewClickListener {


    // Static Properties
    //
    private static final String BUNDLE_EXERCISE_ID = "exerciseIdBundle";
    private static final String SAVE_STATE_WEIGHT = "saveStateWeight";
    private static final String SAVE_STATE_REPS = "saveStateReps";


    // Instance
    //
    public static WorkoutTrackerFragment newInstance(int exerciseId)
    {
        WorkoutTrackerFragment frag = new WorkoutTrackerFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(BUNDLE_EXERCISE_ID, exerciseId);
        frag.setArguments(bundle);

        return frag;
    }


    // Private Properties
    //
    private View rootView;
    private WorkoutTrackerContract.Presenter presenter;

    private TrackerAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private Toast toast;


    // Binds
    //
    @Bind(R.id.date)
    TextView dateTextView;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.first_button)
    AppCompatButton firstButton;

    @Bind(R.id.second_button)
    AppCompatButton secondButton;

    @Bind(R.id.rep_number_picker)
    NumberPicker repNumberPicker;

    @Bind(R.id.weight_number_picker)
    NumberPicker weightNumberPicker;


    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_workout_tracker, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Injector.getAppComponent().inject(this);

        presenter = new WorkoutTrackerPresenter(this, getArguments().getInt(BUNDLE_EXERCISE_ID, Bundles.SHIT_ID));
        presenter.viewCreated();


        toast = Toast.makeText(getActivity(), null, Toast.LENGTH_SHORT);
        resetButtons();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(saveState != null) {
            weightNumberPicker.setNumber(saveState.getDouble(SAVE_STATE_WEIGHT));
            repNumberPicker.setNumber(saveState.getInt(SAVE_STATE_REPS));
            saveState = null;
        }
    }

    @Override
    protected Bundle saveState() {
        Bundle bundle = new Bundle();
        bundle.putDouble(SAVE_STATE_WEIGHT, weightNumberPicker.getNumberAsDouble());
        bundle.putInt(SAVE_STATE_REPS, repNumberPicker.getNumberAsInt());
        return bundle;
    }

    @OnClick(R.id.first_button)
    public void firstButtonOnClick() {

        if(repNumberPicker.getNumberAsInt() < 1) {
            toast.setText(R.string.workout_error_one_rep_needed);
            toast.show();
            return;
        }

        presenter.onSave(repNumberPicker.getNumber(), weightNumberPicker.getNumber());
        EventBus.getDefault().post(new TrackerEvent(false, true));
    }

    @OnClick(R.id.second_button)
    public void secondButtonOnClick() {
        presenter.onButtonTwoPressed();
    }


    // Private Functions
    //
    private void resetButtons() {
        firstButton.setText(getString(R.string.save));
        secondButton.setText(getString(R.string.clear));
    }

    @Override
    public void updateIncrements(double increment) {
        weightNumberPicker.setIncrement(increment);
    }

    @Override
    public void updateValues(float weight, int reps) {
        weightNumberPicker.setNumber(weight);
        repNumberPicker.setNumber(reps);
    }

    @Override
    public void onClick(View v, int position) {
        presenter.onSelect(position);
        mAdapter.selected(position);
    }

    @Override
    public void onLongClick(View v, int position) {
        presenter.onSelect(position);
        mAdapter.selected(position);
        showDeleteRepAlert(position);
    }

    @Override
    public void onListUpdated(boolean isEmpty, boolean isClear) {
    }

    private void updateValues(TrackerListModel model) {
        weightNumberPicker.setNumber((float)model.getWeight());
        repNumberPicker.setNumber(model.getCount());
    }

    @Override
    public void setDate(int titleRes, String title) {

        if(title == null)
            dateTextView.setText(getString(titleRes));
        else
            dateTextView.setText(title);
    }

    // Contract
    //
    @Override
    public void updateAdapter(List<TrackerListModel> data) {

        if (mAdapter == null) {

            mAdapter = new TrackerAdapter(getActivity(), data, this);
            mRecyclerView.setAdapter(mAdapter);
            layoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(layoutManager);
        }
        else {
            mAdapter.setList(data);
        }

        EventBus.getDefault().post(new TrackerEvent(true));
    }

    @Override
    public String getRepsLabel(boolean isMultiple) {
        return isMultiple ? getString(R.string.reps) : getString(R.string.rep);
    }

    @Override
    public void saveSuccess(int position, boolean isNew) {
        mAdapter.clearSelected();
        layoutManager.scrollToPosition(position);
        resetButtons();

        if (isNew)
            toast.setText(R.string.workout_set_created_toast);
        else
            toast.setText(getString(R.string.workout_set_updated_toast, Integer.toString(position + 1)));

        toast.show();
    }

    @Override
    public void onDelete(int position) {
        toast.setText(getString(R.string.workout_set_deleted_toast, Integer.toString(position + 1)));
        toast.show();
    }

    @Override
    public void deleteSetSuccess() {
        getNavigationManager().navigateBack(getActivity());
    }

    @Override
    public void onSelect(TrackerListModel rep) {
        updateValues(rep);

        firstButton.setText(getString(R.string.update));
        secondButton.setText(getString(R.string.cancel));
    }

    public void showDeleteRepAlert(final int position) {

        new MaterialDialog.Builder(getActivity())
                .title(R.string.dialog_tracker_delete_rep_title)
                .content(R.string.dialog_tracker_delete_rep_message)
                .neutralText(R.string.delete)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.onDeleteRep(position);
                    }
                })
                .positiveText(R.string.cancel).show();
    }

    @Override
    public void clear(boolean clearValues, TrackerListModel model) {
        mAdapter.clearSelected();
        resetButtons();

        if (clearValues) {
            repNumberPicker.setNumber(0);
            weightNumberPicker.setNumber(0);
        }
        else {
            if (model != null) {
                repNumberPicker.setNumber(model.getCount());
                weightNumberPicker.setNumber(model.getWeight());
            }
        }
    }


    // Subscriptions
    //

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onIncrementEvent(IncrementEvent event) {
        presenter.updateIncrement();
    }
}
