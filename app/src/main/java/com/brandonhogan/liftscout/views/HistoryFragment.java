package com.brandonhogan.liftscout.views;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.events.TrackerEvent;
import com.brandonhogan.liftscout.interfaces.contracts.HistoryContract;
import com.brandonhogan.liftscout.models.HistoryListItemModel;
import com.brandonhogan.liftscout.models.HistoryListSectionModel;
import com.brandonhogan.liftscout.presenters.WorkoutHistoryPresenter;
import com.brandonhogan.liftscout.utils.DateUtil;
import com.brandonhogan.liftscout.utils.constants.Bundles;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

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


    //Bindings
    //
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.title)
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

        presenter = new WorkoutHistoryPresenter(this, getArguments().getInt(BUNDLE_EXERCISE_ID, Bundles.SHIT_ID));
        presenter.viewCreated();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    //Contracts
    //
    @Override
    public void setupAdapter(List<HistoryListSectionModel> data) {
        mAdapter = new FastItemAdapter<>();
        mAdapter.withSelectable(true);
        mAdapter.add(data);

        mAdapter.withOnClickListener(new FastAdapter.OnClickListener<HistoryListItemModel>() {
            @Override
            public boolean onClick(View v, IAdapter<HistoryListItemModel> adapter, HistoryListItemModel item, int position) {
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
                title.setText(DateUtil.toSimpleStringDate(start));
            else
                title.setText(DateUtil.toSimpleDateRange(start, end));
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

    private void showEditDialog(final HistoryListItemModel item) {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.dialog_edit_history_title)
                .content(R.string.dialog_edit_history_message)
                .neutralText(R.string.edit)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.setClicked(item.exerciseId, item.date);
                    }
                })
                .positiveText(R.string.cancel).show();
    }
}
