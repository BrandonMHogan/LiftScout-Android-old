package com.brandonhogan.liftscout.fragments.workout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.model.Exercise;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.fragments.base.BaseFragment;

import java.util.Date;

import butterknife.Bind;

public class WorkoutContainerFragment extends BaseFragment {

    // Static Properties
    //
    private static final String BUNDLE_EXERCISE_ID = "exerciseIdBundle";
    private static final String BUNDLE_DATE = "dateBundle";
    private static final String BUNDLE_SET_ID = "setIdBundle";
    private static final int SHIT_ID = 99999;


    // Instance
    //
    public static WorkoutContainerFragment newInstance(int setId)
    {
        WorkoutContainerFragment frag = new WorkoutContainerFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(BUNDLE_SET_ID, setId);
        frag.setArguments(bundle);

        return frag;
    }

    public static WorkoutContainerFragment newInstance(int exerciseId, Date date)
    {
        WorkoutContainerFragment frag = new WorkoutContainerFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(BUNDLE_EXERCISE_ID, exerciseId);
        bundle.putSerializable(BUNDLE_DATE, date);
        frag.setArguments(bundle);

        return frag;
    }


    // Private Properties
    //
    private View rootView;
    private int setId;
    private int exerciseId;
    private Date date;
    private Set _set;


    // Binds
    //
    @Bind(R.id.workout_viewpager)
    ViewPager viewPager;

    @Bind(R.id.workout_tab_layout)
    TabLayout tabLayout;


    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_workout_container, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setId = getArguments().getInt(BUNDLE_SET_ID, SHIT_ID);

        try {
            exerciseId = getArguments().getInt(BUNDLE_EXERCISE_ID, SHIT_ID);
            date = (Date)getArguments().getSerializable(BUNDLE_DATE);

        } catch (Exception ex) {
        }


        setTitle(getSet().getExercise().getName());


        tabLayout.addTab(tabLayout.newTab().setText("Tracker"));
        tabLayout.addTab(tabLayout.newTab().setText("History"));
        tabLayout.addTab(tabLayout.newTab().setText("History 2"));
        tabLayout.addTab(tabLayout.newTab().setText("History 3"));
        tabLayout.addTab(tabLayout.newTab().setText("History 4"));
        tabLayout.addTab(tabLayout.newTab().setText("History 5"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        final WorkoutContainerAdapter adapter = new WorkoutContainerAdapter
                (getChildFragmentManager(), setId);

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private Set getSet() {
        if (_set != null)
            return _set;

        if (setId == SHIT_ID) {
            _set = new Set();
            _set.setExercise(getRealm().where(Exercise.class).equalTo(Exercise.ID, exerciseId).findFirst());
        }
        else {
            _set = getRealm().where(Set.class).equalTo(Set.ID, setId).findFirst();
        }

        return _set;
    }

}
