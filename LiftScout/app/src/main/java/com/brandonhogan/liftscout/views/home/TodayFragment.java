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
import com.brandonhogan.liftscout.core.model.Progress;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.core.utils.Constants;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.brandonhogan.liftscout.views.home.workout.WorkoutItem;
import com.brandonhogan.liftscout.views.home.workout.WorkoutSection;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.mikepenz.fastadapter_extensions.drag.ItemTouchCallback;
import com.mikepenz.fastadapter_extensions.drag.SimpleDragCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;

public class TodayFragment extends BaseFragment implements ItemTouchCallback {


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
    private TextView weightView;
    private LinearLayout weightLayout;
    private Date date;
    private String year;
    private String dateString;
    private FastItemAdapter mAdapter;
    List<WorkoutSection> _workout;


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

        date = new Date(getArguments().getLong(DATE_BUNDLE));

        dateString = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT, Locale.getDefault()).format(date);
        year = new SimpleDateFormat(Constants.SIMPLE_DATE_YEAR_FORMAT, Locale.getDefault()).format(date);

        setTitle();
        setWeight();
        setupAdapter();
    }


    // Private Functions
    //
    private void clearLocalReferences() {
        _workout = null;
    }

    private void setTitle() {
        dateView.setText(dateString);
        dateYearView.setText(year);
    }

    private void setWeight() {
        double weight = getTodayProgress().getWeight();

        if (weight == 0)
            weightLayout.setVisibility(View.GONE);
        else {
            weightLayout.setVisibility(View.VISIBLE);
            weightView.setText(Double.toString(weight));
        }
    }

    @SuppressWarnings("unchecked")
    private void setupAdapter() {

        mAdapter = new FastItemAdapter<>();


        mAdapter.withSelectable(true);

        mAdapter.add(getData());

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

        // Checks the manager to see if a set has been updated.
        // If so, it will check the current sets to see if it matches, and updates it
        Set updatedSet = getProgressManager().getUpdatedSet();
        if (updatedSet != null) {
            int pos = 0;
            for (WorkoutSection section : _workout) {
                if (section.setId == updatedSet.getId()) {
                    mAdapter.expand(pos);
                    getProgressManager().clearUpdatedSet();
                    break;
                }
                pos +=1;
            }
        }
    }

    @Override
    public boolean itemTouchOnMove(int oldPosition, int newPosition) {
        if (mAdapter.getAdapterItem(newPosition) instanceof WorkoutSection) {
            Collections.swap(mAdapter.getAdapterItems(), oldPosition, newPosition); // change position

            WorkoutSection sectionA = ((WorkoutSection)mAdapter.getAdapterItem(newPosition));
            WorkoutSection sectionB = ((WorkoutSection)mAdapter.getAdapterItem(oldPosition));

            // Will update the orderId of the sets and save to realm
            getRealm().beginTransaction();
            Set setA = getTodayProgress().getSets().where().equalTo(Set.ID, sectionA.setId).findFirst();
            Set setB = getTodayProgress().getSets().where().equalTo(Set.ID, sectionB.setId).findFirst();

            int orderA = setA.getOrderId();
            int orderB = setB.getOrderId();

            setA.setOrderId(orderB);
            setB.setOrderId(orderA);
            getRealm().commitTransaction();

            mAdapter.notifyAdapterItemMoved(oldPosition, newPosition);
        }
        return true;
    }


    private List<WorkoutSection> getData() {

        if (_workout != null && _workout.size() > 0 && !getProgressManager().isSetUpdated())
            return _workout;

        _workout = new ArrayList<>();

        for (Set set : getTodayProgress().getSets().sort(Set.ORDER_ID)) {

            List<IItem> items = new LinkedList<>();
            double volume = 0;

            for (Rep rep : set.getReps()) {
                items.add(new WorkoutItem(set.getId(), set.getExercise().getId(), rep.getCount(), rep.getWeight()));
                volume += rep.getWeight();
            }

            WorkoutSection expandableItem = new WorkoutSection(set.getId(), set.getExercise().getName(), volume);
            expandableItem.withSubItems(items);
            _workout.add(expandableItem);
        }
        return _workout;
    }

    private Progress getTodayProgress() {
        Progress _currentProgress = getRealm().where(Progress.class)
                .equalTo(Progress.DATE, date).findFirst();

        if (_currentProgress == null) {
            _currentProgress = new Progress();
            _currentProgress.setDate(date);

            getRealm().beginTransaction();
            getRealm().copyToRealmOrUpdate(_currentProgress);
            getRealm().commitTransaction();
        }

        return _currentProgress;
    }


    // Public Function
    //
    @SuppressWarnings("unchecked")
    public void update() {
        clearLocalReferences();
        setWeight();
        mAdapter.setNewList(getData());
    }
}