package com.brandonhogan.liftscout.views.workout.tracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.constants.Bundles;
import com.brandonhogan.liftscout.core.controls.NumberPicker;
import com.brandonhogan.liftscout.core.utils.BhDate;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.brandonhogan.liftscout.views.workout.TrackerEvent;
import com.nikhilpanju.recyclerviewenhanced.OnActivityTouchListener;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import org.greenrobot.eventbus.EventBus;

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
    private MenuItem deleteMenu;


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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        deleteMenu = menu.findItem(R.id.action_delete);
        deleteMenu.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                presenter.onDelete();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
    }

    @Override
    public void onPause() {
        super.onPause();
        mRecyclerView.removeOnItemTouchListener(onTouchListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (deleteMenu != null)
            deleteMenu.setVisible(false);
    }

    @Override
    public void setOnActivityTouchListener(OnActivityTouchListener listener) {
        this.touchListener = listener;
    }

    @OnClick(R.id.first_button)
    public void firstButtonOnClick() {
        presenter.onSave(repNumberPicker.getNumber(), weightNumberPicker.getNumber());
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
    public void showDeleteSetAlert() {
        String message =
                String.format(getString(R.string.dialog_tracker_delete_set_message)
                        , presenter.getExerciseName());

        dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.dialog_tracker_delete_set_title))
                .setContentText(message)
                .setConfirmText(getString(R.string.delete))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        dialog.cancel();
                        presenter.onDeleteSet();
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
}
