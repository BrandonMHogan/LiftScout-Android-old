package com.brandonhogan.liftscout.fragments.workout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.constants.Bundles;
import com.brandonhogan.liftscout.core.model.Exercise;
import com.brandonhogan.liftscout.fragments.base.BaseFragment;

import butterknife.Bind;

public class WorkoutContainerFragment extends BaseFragment {


    // Static Properties
    //
    private static final String BUNDLE_EXERCISE_ID = "exerciseIdBundle";


    // Instance
    //
    public static WorkoutContainerFragment newInstance(int exerciseId)
    {
        WorkoutContainerFragment frag = new WorkoutContainerFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(BUNDLE_EXERCISE_ID, exerciseId);
        frag.setArguments(bundle);

        return frag;
    }


    // Private Properties
    //
    private View rootView;
    private int exerciseId;


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

        exerciseId = getArguments().getInt(BUNDLE_EXERCISE_ID, Bundles.SHIT_ID);

        setTitle(getTitle());

        tabLayout.addTab(tabLayout.newTab().setText("Tracker"));
        tabLayout.addTab(tabLayout.newTab().setText("History"));
        tabLayout.addTab(tabLayout.newTab().setText("History 2"));
        tabLayout.addTab(tabLayout.newTab().setText("History 3"));
        tabLayout.addTab(tabLayout.newTab().setText("History 4"));
        tabLayout.addTab(tabLayout.newTab().setText("History 5"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        final WorkoutContainerAdapter adapter = new WorkoutContainerAdapter
                (getChildFragmentManager(), exerciseId);

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

    private String getTitle() {
        return getRealm().where(Exercise.class).equalTo(Exercise.ID, exerciseId).findFirst().getName();
    }

}
