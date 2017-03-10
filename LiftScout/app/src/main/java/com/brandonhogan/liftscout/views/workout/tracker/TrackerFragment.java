package com.brandonhogan.liftscout.views.workout.tracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.constants.Bundles;
import com.brandonhogan.liftscout.core.controls.NumberPicker;
import com.brandonhogan.liftscout.core.utils.BhDate;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.brandonhogan.liftscout.views.workout.IncrementEvent;
import com.brandonhogan.liftscout.views.workout.TrackerEvent;
import com.nikhilpanju.recyclerviewenhanced.OnActivityTouchListener;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class TrackerFragment extends BaseFragment implements
        TrackerContract.View, RecyclerTouchListener.RecyclerTouchListenerHelper {


    // Static Properties
    //
    private static final String BUNDLE_EXERCISE_ID = "exerciseIdBundle";
    private static final String SAVE_STATE_WEIGHT = "saveStateWeight";
    private static final String SAVE_STATE_REPS = "saveStateReps";


    // Instance
    //
    public static TrackerFragment newInstance(int exerciseId)
    {
        TrackerFragment frag = new TrackerFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(BUNDLE_EXERCISE_ID, exerciseId);
        frag.setArguments(bundle);

        return frag;
    }


    // Private Properties
    //
    private View rootView;
    private TrackerContract.Presenter presenter;

    private TrackerAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private RecyclerTouchListener onTouchListener;
    private OnActivityTouchListener touchListener;
    private SweetAlertDialog dialog;


    // Binds
    //
    @Bind(R.id.date)
    TextView dateTextView;

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
        rootView = inflater.inflate(R.layout.frag_tracker, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Injector.getAppComponent().inject(this);

        presenter = new TrackerPresenter(this, getArguments().getInt(BUNDLE_EXERCISE_ID, Bundles.SHIT_ID));
        presenter.viewCreated();

        resetButtons();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(saveState != null) {
            weightNumberPicker.setNumber(Float.valueOf(saveState.getString(SAVE_STATE_WEIGHT)));
            repNumberPicker.setNumber(Integer.valueOf(saveState.getString(SAVE_STATE_REPS)));
            saveState = null;
        }
    }

    @Override
    protected Bundle saveState() {
        Bundle bundle = new Bundle();
        bundle.putString(SAVE_STATE_WEIGHT, weightNumberPicker.getNumber());
        bundle.putString(SAVE_STATE_REPS, repNumberPicker.getNumber());
        return bundle;
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView.addOnItemTouchListener(onTouchListener);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mRecyclerView.removeOnItemTouchListener(onTouchListener);
        EventBus.getDefault().unregister(this);
       //presenter.onRestTimerStop();
    }

    @Override
    public void setOnActivityTouchListener(OnActivityTouchListener listener) {
        this.touchListener = listener;
    }

    @OnClick(R.id.first_button)
    public void firstButtonOnClick() {

        if(repNumberPicker.getNumberAsInt() < 1) {
            Toast.makeText(getActivity(), R.string.workout_error_one_rep_needed, Toast.LENGTH_SHORT).show();
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

    private void updateValues(TrackerListModel model) {
        weightNumberPicker.setNumber((float)model.getWeight());
        repNumberPicker.setNumber(model.getCount());
    }

    @Override
    public void setDate(Date date) {
        dateTextView.setText(BhDate.toSimpleStringDate(date));
    }

    // Contract
    //
    @Override
    public void updateAdapter(List<TrackerListModel> data) {

        if (mAdapter == null) {

            mAdapter = new TrackerAdapter(getActivity(), data);
            mRecyclerView.setAdapter(mAdapter);

            layoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(layoutManager);

            onTouchListener = new RecyclerTouchListener(getActivity(), mRecyclerView);

            onTouchListener
                    .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                        @Override
                        public void onRowClicked(int position) {
                            presenter.onSelect(position);
                            mAdapter.selected(position);
                        }

                        @Override
                        public void onIndependentViewClicked(int independentViewID, int position) {
                        }
                    });

            onTouchListener.setLongClickable(true, new RecyclerTouchListener.OnRowLongClickListener() {
                @Override
                public void onRowLongClicked(int position) {
                    presenter.onSelect(position);
                    mAdapter.selected(position);
                    showDeleteRepAlert();
                }
            });


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
    public void saveSuccess(int position) {
        mAdapter.clearSelected();
        layoutManager.scrollToPosition(position);
        resetButtons();
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

    @Override
    public void showDeleteRepAlert() {

        dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.dialog_tracker_delete_rep_title))
                .setContentText(getString(R.string.dialog_tracker_delete_rep_message))
                .setConfirmText(getString(R.string.delete))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        dialog.cancel();
                        presenter.onDeleteRep();
                    }
                })
                .setCancelText(getString(R.string.cancel))
                .showCancelButton(true);

        dialog.show();
    }

    @Override
    public void clear(boolean clearValues) {
        mAdapter.clearSelected();
        resetButtons();

        if (clearValues) {
            repNumberPicker.setNumber(0);
            weightNumberPicker.setNumber(0);
        }
    }


    // Subscriptions
    //

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onIncrementEvent(IncrementEvent event) {
        presenter.updateIncrement();
    }
}
