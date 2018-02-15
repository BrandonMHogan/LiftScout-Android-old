package com.brandonhogan.liftscout.views;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.adapters.ExerciseListContainerAdapter;
import com.brandonhogan.liftscout.interfaces.contracts.ExerciseListContainerContract;
import com.brandonhogan.liftscout.presenters.ExerciseListContainerPresenter;
import com.brandonhogan.liftscout.views.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;


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
    private ExerciseListContainerAdapter adapter;


    // Binds
    //
    @BindView(R.id.workout_viewpager)
    ViewPager viewPager;

    @BindView(R.id.workout_tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.fab)
    FloatingActionButton fab;


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

        setTitle(getString(R.string.nav_exercises));

        if (presenter == null) {
            presenter = new ExerciseListContainerPresenter(this,
                    getArguments().getBoolean(BUNDLE_ADD_SET));
            presenter.viewCreated();
        }

        if (adapter == null) {
            adapter = new ExerciseListContainerAdapter
                    (getChildFragmentManager(), presenter.isAddSet());
        }


        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.exercises_all)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.exercises_categories)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.exercises_favourites)));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 2)
                    fab.hide();
                else
                    fab.show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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
        menu.findItem(R.id.action_search).setVisible(true);
    }

    @Override
    public void onCreateCategory() {
        getNavigationManager().startCategoryDetail();
    }

    @Override
    public void onNoCategoryFound() {
        fab.hide();
        new MaterialDialog.Builder(getActivity())
                .title(R.string.exercise_no_categories_found_title)
                .content(R.string.exercise_no_categories_found_message)
                .neutralText(R.string.create)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        onCreateCategory();
                    }
                })
                .positiveText(R.string.cancel)
                .canceledOnTouchOutside(true)
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        fab.show();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        fab.show();
                    }
                }).show();
    }

    @Override
    public void onCreateExercise() {
        getNavigationManager().startExerciseDetail(false, 0);
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        presenter.onFabClicked(viewPager.getCurrentItem());
    }

}
