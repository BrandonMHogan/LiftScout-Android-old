package com.brandonhogan.liftscout.views.workout.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.constants.Bundles;
import com.brandonhogan.liftscout.core.utils.BhDate;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.brandonhogan.liftscout.views.workout.TrackerEvent;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class HistoryFragment extends BaseFragment implements HistoryContract.View {

    // Static Properties
    //
    private static final String BUNDLE_EXERCISE_ID = "exerciseIdBundle";

    // Instance
    //
    public static HistoryFragment newInstance(int exerciseId)
    {
        HistoryFragment frag = new HistoryFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(BUNDLE_EXERCISE_ID, exerciseId);
        frag.setArguments(bundle);

        return frag;
    }


    // Private Properties
    //
    private View rootView;
    private HistoryContract.Presenter presenter;
    private FastItemAdapter mAdapter;
    private SweetAlertDialog dialog;


    //Bindings
    //
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.title)
    TextView title;


    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_workout_history, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new HistoryPresenter(this, getArguments().getInt(BUNDLE_EXERCISE_ID, Bundles.SHIT_ID));
        presenter.viewCreated();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    //Contracts
    //
    @Override
    public void setupAdapter(List<HistoryListSection> data) {
        mAdapter = new FastItemAdapter<>();
        mAdapter.withSelectable(true);
        mAdapter.add(data);

        mAdapter.withOnClickListener(new FastAdapter.OnClickListener<HistoryListItem>() {
            @Override
            public boolean onClick(View v, IAdapter<HistoryListItem> adapter, HistoryListItem item, int position) {
                showEditDialog(item);
                return true;
            }
        });

        mAdapter.withOnLongClickListener(new FastAdapter.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v, IAdapter adapter, IItem item, int position) {
                mAdapter.collapse();
                return false;
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void setupTitle(long start, long end) {
        if (start > 0 && end > 0) {
            if (start == end)
                title.setText(BhDate.toSimpleStringDate(start));
            else
                title.setText(BhDate.toSimpleDateRange(start, end));
        }
    }

    @Override
    public void editTracker(int exerciseId) {
        getNavigationManager().startWorkoutContainer(exerciseId, true);
    }

    @Override
    public String getEmptySetMessage() {
        return getString(R.string.frag_history_empty_set_msg);
    }


    // Bus Subscriptions
    //
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTrackerEvent(TrackerEvent event) {
        if (event.isUpdated)
            presenter.update();
    }


    // Private Functions
    //

    private void showEditDialog(final HistoryListItem item) {
        dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(getString(R.string.dialog_edit_history_title))
                .setContentText(getString(R.string.dialog_edit_history_message))
                .setConfirmText(getString(R.string.edit))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        dialog.cancel();
                        presenter.setClicked(item.exerciseId, item.date);
                    }
                })
                .setCancelText(getString(R.string.cancel))
                .showCancelButton(true);

        dialog.show();
    }
}
