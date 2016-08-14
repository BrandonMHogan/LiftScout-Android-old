package com.brandonhogan.liftscout.fragments.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.model.Progress;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.fragments.base.BaseFragment;
import com.brandonhogan.liftscout.fragments.home.today.WorkoutItem;
import com.brandonhogan.liftscout.fragments.home.today.WorkoutSection;
import com.brandonhogan.liftscout.fragments.home.today.WorkoutSectionAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;

public class TodayFragment extends BaseFragment {


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
    private View rootView;
    private TextView weightView;
    private LinearLayout weightLayout;
    private long dateLong;
    private Date date;
    private String year;
    private String dateString;
    private WorkoutSectionAdapter mAdapter;
    List<WorkoutSection> sections;

    private Progress _currentProgress;


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
        rootView = inflater.inflate(R.layout.frag_today, container, false);

        weightView = (TextView) rootView.findViewById(R.id.weightView);
        weightLayout = (LinearLayout) rootView.findViewById(R.id.weight_layout);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dateLong = getArguments().getLong(DATE_BUNDLE);
        date = new Date(dateLong);

        dateString = new SimpleDateFormat("EEE, MMMM d", Locale.getDefault()).format(date);
        year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(date);

        setTitle();
        setWeight();
        setupAdapter();
    }


    // Private Functions
    //
    private void clearLocalReferences() {
        _currentProgress = null;
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

    private void setupAdapter() {
        mAdapter = new WorkoutSectionAdapter(getActivity(), getData());
        mAdapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
            @Override
            public void onListItemExpanded(int position) {
                WorkoutSection expandedRecipe = getData().get(position);

                String toastMsg = "expanded: " + expandedRecipe.getName();
                Toast.makeText(getActivity(),
                        toastMsg,
                        Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onListItemCollapsed(int position) {
                WorkoutSection collapsedRecipe = getData().get(position);

                String toastMsg = "collapsed: " + collapsedRecipe.getName();
                Toast.makeText(getActivity(),
                        toastMsg,
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private List<WorkoutSection> getData() {

        if (sections != null)
            return sections;

        sections = new ArrayList<>();

        for (Set set : getTodayProgress().getSets()) {

            double volume = 0;
            ArrayList<WorkoutItem> items = new ArrayList<>();
            for (Rep rep : set.getReps()) {
                items.add(new WorkoutItem(rep.getCount(), rep.getWeight()));
                volume += rep.getWeight();
            }

            sections.add(new WorkoutSection(set.getExercise().getName(), volume, items));
        }
        return sections;
    }

    private Progress getTodayProgress() {
        if (_currentProgress != null && _currentProgress.isValid())
            return _currentProgress;

        _currentProgress = getRealm().where(Progress.class)
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
    public void update() {
        clearLocalReferences();
        setWeight();
       // mAdapter.setList(getData());
    }
}