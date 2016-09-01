package com.brandonhogan.liftscout.views.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.brandonhogan.liftscout.views.home.workout.WorkoutItem;
import com.brandonhogan.liftscout.views.home.workout.WorkoutSection;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.mikepenz.fastadapter_extensions.drag.ItemTouchCallback;
import com.mikepenz.fastadapter_extensions.drag.SimpleDragCallback;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

public class TodayFragment extends BaseFragment implements TodayContact.View, ItemTouchCallback {


    // Instance
    //
    public static TodayFragment newInstance(Date date)
    {
        TodayFragment frag = new TodayFragment();
        Bundle bundle = new Bundle();

        bundle.putLong(DATE_BUNDLE, date.getTime());
        frag.setArguments(bundle);

        return frag;
    }


    // Static Properties
    //
    private static final String DATE_BUNDLE = "dateBundle";


    // Private Properties
    //
    private TodayPresenter presenter;
    private TextView weightView;
    private LinearLayout weightLayout;
    private FastItemAdapter mAdapter;


    // Binds
    //
    @Bind(R.id.date)
    TextView dateView;

    @Bind(R.id.date_year)
    TextView dateYearView;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    

    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_today, container, false);

        weightView = (TextView) view.findViewById(R.id.weightView);
        weightLayout = (LinearLayout) view.findViewById(R.id.weight_layout);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Injector.getAppComponent().inject(this);
        presenter = new TodayPresenter(this, getArguments().getLong(DATE_BUNDLE));
        presenter.viewCreate();
    }

    @Override
    public boolean itemTouchOnMove(int oldPosition, int newPosition) {
        if (mAdapter.getAdapterItem(newPosition) instanceof WorkoutSection) {
            Collections.swap(mAdapter.getAdapterItems(), oldPosition, newPosition); // change position

            WorkoutSection sectionA = ((WorkoutSection)mAdapter.getAdapterItem(newPosition));
            WorkoutSection sectionB = ((WorkoutSection)mAdapter.getAdapterItem(oldPosition));

            presenter.itemTouchOnMove(sectionA.setId, sectionB.setId);

            mAdapter.notifyAdapterItemMoved(oldPosition, newPosition);
        }
        return true;
    }

    // Public Functions
    public void update() {
        presenter.update();
    }


    // Contracts
    //
    @Override
    public void setupTitle(String date, String year) {
        dateView.setText(date);
        dateYearView.setText(year);
    }

    @Override
    public void setupWeight(String weight) {
        if (weight == null)
            weightLayout.setVisibility(View.GONE);
        else {
            weightLayout.setVisibility(View.VISIBLE);
            weightView.setText(weight);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setupAdapter(List<WorkoutSection> data, int expandPosition) {

        mAdapter = new FastItemAdapter<>();
        mAdapter.withSelectable(true);
        mAdapter.add(data);

        mAdapter.withOnClickListener(new FastAdapter.OnClickListener<WorkoutItem>() {
            @Override
            public boolean onClick(View v, IAdapter<WorkoutItem> adapter, WorkoutItem item, int position) {
                getNavigationManager().startWorkoutContainer(item.exerciseId);
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

        // Enabled Drag and drop
        SimpleDragCallback touchCallback = new SimpleDragCallback(this);
        ItemTouchHelper touchHelper = new ItemTouchHelper(touchCallback);
        touchHelper.attachToRecyclerView(mRecyclerView);

        mAdapter.expand(expandPosition);
    }
}