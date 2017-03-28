package com.brandonhogan.liftscout.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.adapters.ExerciseListAdapter;
import com.brandonhogan.liftscout.interfaces.RecyclerViewClickListener;
import com.brandonhogan.liftscout.interfaces.contracts.ExerciseListContract;
import com.brandonhogan.liftscout.models.ExerciseListModel;
import com.brandonhogan.liftscout.presenters.ExerciseListPresenter;
import com.brandonhogan.liftscout.repository.model.Exercise;
import com.brandonhogan.liftscout.views.base.BaseFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

public class ExerciseListFragment extends BaseFragment implements
        ExerciseListContract.View,
        RecyclerViewClickListener {


    // Private Static Properties
    //
    private final static String BUNDLE_CATEGORY_ID = "categoryIdBundle";
    private final static String BUNDLE_ADD_SET = "addSetBundle";


    // Instances
    //
    public static ExerciseListFragment newInstance(int categoryId, boolean addSet) {

        Bundle args = new Bundle();
        args.putInt(BUNDLE_CATEGORY_ID, categoryId);
        args.putBoolean(BUNDLE_ADD_SET, addSet);

        ExerciseListFragment fragment = new ExerciseListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public static ExerciseListFragment newInstance(int categoryId) {
        Bundle args = new Bundle();
        args.putInt(BUNDLE_CATEGORY_ID, categoryId);

        ExerciseListFragment fragment = new ExerciseListFragment();
        fragment.setArguments(args);

        return fragment;
    }


    // Private Properties
    //
    private View rootView;
    private ExerciseListContract.Presenter presenter;
    private ExerciseListAdapter mAdapter;
    private MaterialDialog dialog;


    // Binds
    //
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.no_data_label)
    TextView noDataLabel;

    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_exercise_list, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new ExerciseListPresenter(this,
                this.getArguments().getInt(BUNDLE_CATEGORY_ID),
                this.getArguments().getBoolean(BUNDLE_ADD_SET));

        presenter.viewCreated();
        fab.setVisibility(presenter.isInSearch() ? View.GONE : View.VISIBLE);
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
    public void searchViewOnQueryTextChange(String newText) {
        mAdapter.filterList(newText);
    }

    @Override
    public void searchViewOnOpen() {
        super.searchViewOnOpen();
    }

    @Override
    public void searchViewOnClose() {
        super.searchViewOnClose();
    }

    @Override
    public void onClick(View v, int position) {
        presenter.rowClicked(position);
    }

    @Override
    public void onLongClick(View v, int position) {
        itemSelectedDialog(position);
    }

    // Private Functions
    //
    private void editExercise(int position) {
        getNavigationManager().startExerciseDetail(presenter.getExercise(position).getId(), presenter.getCategoryId());
    }

    private void createExercise() {
        getNavigationManager().startExerciseDetail(presenter.getCategoryId());
    }

    private void itemSelectedDialog(final int position) {

        Exercise exercise = presenter.getExercise(position);

        dialog = new MaterialDialog.Builder(getActivity())
                .title(exercise.getName())
                .content(getString(R.string.dialog_category_long_selected, exercise.getName()))
                .positiveText(R.string.edit)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        editExercise(position);
                        dialog.cancel();
                    }
                })
                .neutralText(R.string.delete)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.deleteExercise(position);
                        dialog.cancel();
                    }
                })
                .build();

        dialog.show();
    }



    // Contracts
    //

    @Override
    public void applyTitle(String title) {
        setTitle(title);
        noDataLabel.setText(String.format(getString(R.string.exercise_list_no_data), title));
    }

    @Override
    public void updateAdapter(ArrayList<ExerciseListModel> data) {

        noDataLabel.setVisibility((data == null || data.isEmpty()) ? View.VISIBLE : View.GONE);

        mAdapter = new ExerciseListAdapter(getActivity(), data, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void removeItem(int position) {
        mAdapter.notifyItemRemoved(position);
    }

    @Override
    public void itemSelected(int exerciseId, boolean isSearch) {

        if (isSearch)
            getNavigationManager().startGraphsContainer(true);
        else
            getNavigationManager().startWorkoutContainerAsOpen(exerciseId);
    }

    @Override
    public void swipeItem(int position) {
        editExercise(position);
        //onTouchListener.openSwipeOptions(position);
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        createExercise();
    }
}
