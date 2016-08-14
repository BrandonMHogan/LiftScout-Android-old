package com.brandonhogan.liftscout.fragments.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.brandonhogan.liftscout.fragments.exercises.ExerciseListAdapter;
import com.brandonhogan.liftscout.fragments.home.today.WorkoutItem;
import com.brandonhogan.liftscout.fragments.home.today.WorkoutSection;
import com.brandonhogan.liftscout.fragments.home.today.WorkoutSectionAdapter;
import com.brandonhogan.liftscout.fragments.home.workout.TodayItem;
import com.brandonhogan.liftscout.fragments.home.workout.TodayItemClickListener;
import com.brandonhogan.liftscout.fragments.home.workout.TodaySection;
import com.brandonhogan.liftscout.fragments.home.workout.TodaySectionedExpandableLayoutHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;

public class TodayFragment extends BaseFragment implements TodayItemClickListener {


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
    private TodayListAdapter mAdapter;

    private Progress _currentProgress;
    private ArrayList<TodayListModel> _workout;


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

    private void setupAdapter() {
//        mAdapter = new TodayListAdapter(getActivity(), getData());
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


//        TodaySectionedExpandableLayoutHelper todaySectionedExpandableLayoutHelper =
//                new TodaySectionedExpandableLayoutHelper(getActivity(),
//                mRecyclerView, this, 1);
//
//
//        for (Set set : getTodayProgress().getSets()) {
//
//            ArrayList<TodayItem> arrayList = new ArrayList<>();
//            double volume = 0;
//
//            for (Rep rep : set.getReps()) {
//                volume += rep.getWeight();
//                arrayList.add(new TodayItem(rep.getId(), rep.getCount(), rep.getWeight()));
//            }
//
//            todaySectionedExpandableLayoutHelper.addSection(set.getId(), set.getExercise().getName(), volume, arrayList);
//
//        }
//
//        todaySectionedExpandableLayoutHelper.notifyDataSetChanged();


        WorkoutSectionAdapter adapter;

        WorkoutItem beef = new WorkoutItem("beef");
        WorkoutItem cheese = new WorkoutItem("cheese");
        WorkoutItem salsa = new WorkoutItem("salsa");
        WorkoutItem tortilla = new WorkoutItem("tortilla");
        WorkoutItem ketchup = new WorkoutItem("ketchup");
        WorkoutItem bun = new WorkoutItem("bun");

        WorkoutSection taco = new WorkoutSection("taco", Arrays.asList(beef, cheese, salsa, tortilla));
        WorkoutSection quesadilla = new WorkoutSection("quesadilla", Arrays.asList(cheese, tortilla));
        WorkoutSection burger = new WorkoutSection("burger", Arrays.asList(beef, cheese, ketchup, bun));

        WorkoutSection burger2 = new WorkoutSection("burger 2", Arrays.asList(beef, cheese, ketchup, bun));
        WorkoutSection burger3 = new WorkoutSection("burger 3", Arrays.asList(beef, cheese, ketchup, bun));
        WorkoutSection burger4 = new WorkoutSection("burger 4", Arrays.asList(beef, cheese, ketchup, bun));


        final List<WorkoutSection> recipes = Arrays.asList(taco, quesadilla, burger, burger2, burger3, burger4);

        adapter = new WorkoutSectionAdapter(getActivity(), recipes);
        adapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
            @Override
            public void onListItemExpanded(int position) {
                WorkoutSection expandedRecipe = recipes.get(position);

                String toastMsg = "expanded: " + expandedRecipe.getName();
                Toast.makeText(getActivity(),
                        toastMsg,
                        Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onListItemCollapsed(int position) {
                WorkoutSection collapsedRecipe = recipes.get(position);

                String toastMsg = "collapsed: " + collapsedRecipe.getName();
                Toast.makeText(getActivity(),
                        toastMsg,
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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

    @Override
    public void todayItemClicked(TodayItem item) {
        Toast.makeText(getActivity(), "Item: " + item.getId() + " clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void todayItemClicked(TodaySection section) {
        Toast.makeText(getActivity(), "Section: " + section.getName() + " clicked", Toast.LENGTH_SHORT).show();
    }

    // Public Function
    //
    public void update() {
        clearLocalReferences();
        setWeight();
       // mAdapter.setList(getData());
    }
}