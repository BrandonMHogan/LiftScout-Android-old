package com.brandonhogan.liftscout.views;

import android.app.NotificationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.adapters.ExerciseListContainerAdapter;
import com.brandonhogan.liftscout.interfaces.contracts.ExerciseListContainerContract;
import com.brandonhogan.liftscout.presenters.ExerciseListContainerPresenter;
import com.brandonhogan.liftscout.views.base.BaseFragment;

import butterknife.Bind;


/**
 * Created by Brandon on 3/29/2017.
 * Description :
 */

public class ExerciseListContainerFragment extends BaseFragment implements ExerciseListContainerContract.View {

    // Private Static Properties
    //
    private final static String BUNDLE_ADD_SET = "addSetBundle";


    // Instance
    //
    public static ExerciseListContainerFragment newInstance(boolean addSet)
    {
        Bundle args = new Bundle();
        args.putBoolean(BUNDLE_ADD_SET, addSet);

        ExerciseListContainerFragment fragment = new ExerciseListContainerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    // Private Properties
    //
    private View rootView;
    private ExerciseListContainerContract.Presenter presenter;


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
        rootView = inflater.inflate(R.layout.frag_exercise_list_container, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new ExerciseListContainerPresenter(this,
                getArguments().getBoolean(BUNDLE_ADD_SET));

        presenter.viewCreated();

        setTitle(getString(R.string.nav_exercises));

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.exercises_all)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.exercises_categories)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.exercises_favourites)));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        final ExerciseListContainerAdapter adapter = new ExerciseListContainerAdapter
                (getChildFragmentManager(), presenter.isAddSet());

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
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

}
