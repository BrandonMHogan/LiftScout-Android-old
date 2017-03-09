package com.brandonhogan.liftscout.views.exercises;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.nikhilpanju.recyclerviewenhanced.OnActivityTouchListener;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import java.util.List;

import butterknife.Bind;

public class ExerciseListFragment extends BaseFragment implements
        ExerciseListContract.View,
        RecyclerTouchListener.RecyclerTouchListenerHelper {


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
    private RecyclerTouchListener onTouchListener;
    private OnActivityTouchListener touchListener;
    private MaterialDialog dialog;


    // Binds
    //
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;


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
        MenuItem addMenu = menu.findItem(R.id.action_add);
        addMenu.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                createExercise();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView.addOnItemTouchListener(onTouchListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mRecyclerView.removeOnItemTouchListener(onTouchListener);
    }

    @Override
    public void setOnActivityTouchListener(OnActivityTouchListener listener) {
        this.touchListener = listener;
    }


    // Private Functions
    //
    private void editExercise(int position) {
        ExerciseEditDialog dialog = new ExerciseEditDialog(getActivity(), new ExerciseEditDialog.ExerciseEditDialogListener() {
            @Override
            public void onCancelExerciseEditDialog() {

            }

            @Override
            public void onSaveExerciseEditDialog(ExerciseListModel exercise) {
                presenter.updateExercise(exercise);
            }
        }, true, presenter.getExercise(position));

        dialog.show();
    }

    private void createExercise() {
        ExerciseEditDialog dialog = new ExerciseEditDialog(getActivity(), new ExerciseEditDialog.ExerciseEditDialogListener() {
            @Override
            public void onCancelExerciseEditDialog() {

            }

            @Override
            public void onSaveExerciseEditDialog(ExerciseListModel exercise) {
                presenter.createExercise(exercise);
            }
        }, true, null);

        dialog.show();
    }

    private void removeExerciseAlert(final int position) {

        ExerciseListModel model = presenter.getExercise(position);

        dialog = new MaterialDialog.Builder(getActivity())
                .title(String.format(getString(R.string.dialog_exercise_remove_title), model.getName()))
                .content(String.format(getString(R.string.dialog_exercise_remove_message), model.getName()))
                .positiveText(R.string.delete)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.deleteExercise(position);
                        dialog.cancel();
                    }
                })
                .negativeText(R.string.cancel)
                .build();

        dialog.show();
    }



    // Contracts
    //

    @Override
    public void applyTitle(String title) {
        setTitle(title);
    }

    @Override
    public void updateAdapter(List<ExerciseListModel> data) {
        mAdapter = new ExerciseListAdapter(getActivity(), data);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        onTouchListener = new RecyclerTouchListener(getActivity(), mRecyclerView);

        onTouchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        presenter.rowClicked(position);
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {
                    }
                })
//                .setLongClickable(true, new RecyclerTouchListener.OnRowLongClickListener() {
//                    @Override
//                    public void onRowLongClicked(int position) {
//                    }
//                })
                .setSwipeOptionViews(R.id.delete, R.id.edit)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {

                        if (viewID == R.id.delete) {
                            removeExerciseAlert(position);
                        } else if (viewID == R.id.edit) {
                            editExercise(position);
                        }
                    }
                });
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
            getNavigationManager().startWorkoutContainer(exerciseId);
    }

    @Override
    public void swipeItem(int position) {
        onTouchListener.openSwipeOptions(position);
    }
}
