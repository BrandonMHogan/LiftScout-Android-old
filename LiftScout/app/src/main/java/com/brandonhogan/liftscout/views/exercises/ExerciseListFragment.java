package com.brandonhogan.liftscout.views.exercises;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.model.Category;
import com.brandonhogan.liftscout.core.model.Exercise;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.nikhilpanju.recyclerviewenhanced.OnActivityTouchListener;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.RealmResults;

public class ExerciseListFragment extends BaseFragment implements RecyclerTouchListener.RecyclerTouchListenerHelper {

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
    private ExerciseListAdapter mAdapter;
    private RecyclerTouchListener onTouchListener;
    private OnActivityTouchListener touchListener;
    SweetAlertDialog dialog;

    private List<ExerciseListModel> _exercises;
    private int categoryId;
    private boolean addSet;


    // Binds
    //
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.fab)
    FloatingActionButton fab;


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

        Bundle bundle = this.getArguments();
        categoryId = bundle.getInt(BUNDLE_CATEGORY_ID, -1);
        addSet = bundle.getBoolean(BUNDLE_ADD_SET);

        setTitle(getCategory().getName());

        setupAdapter();
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

    @OnClick(R.id.fab)
    public void addOnClick() {
        createExercise();
    }

    // Private Functions
    //
    private void setupAdapter() {
        mAdapter = new ExerciseListAdapter(getActivity(), getData());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        onTouchListener = new RecyclerTouchListener(getActivity(), mRecyclerView);

        onTouchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        // TODO : If coming from workout, this should select and redirect back to workout
                        // else, just open the swipe menu

                        if (addSet) {
                            addSet(position);
                        }
                        else {
                            onTouchListener.openSwipeOptions(position);
                        }
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

    private List<ExerciseListModel> getData() {

        if (_exercises != null)
            return _exercises;

        _exercises = new ArrayList<>();

        for (Exercise exercise : getExercises().sort(Category.NAME)) {
            _exercises.add(new ExerciseListModel(exercise));
        }

        return _exercises;
    }

    private void update() {
        _exercises = null;
        mAdapter.setList(getData());
    }

    private RealmResults<Exercise> getExercises() {
        return getRealm().where(Exercise.class).equalTo(Exercise.CATEGORY_ID, categoryId).findAll();
    }

    private Category getCategory() {
        return getRealm().where(Category.class).equalTo(Category.ID, categoryId).findFirst();
    }

    private void saveExercise(Exercise exercise) {
        getRealm().beginTransaction();
        getRealm().copyToRealmOrUpdate(exercise);
        getRealm().commitTransaction();

        update();
    }

    private void removeExercise(int id) {
        getRealm().beginTransaction();
        getRealm().where(Exercise.class).equalTo(Exercise.ID, id).findFirst().deleteFromRealm();
        getRealm().commitTransaction();

        update();
    }

    private void editExercise(int position) {
        ExerciseEditDialog dialog = new ExerciseEditDialog(getActivity(), new ExerciseEditDialog.ExerciseEditDialogListener() {
            @Override
            public void onCancelExerciseEditDialog() {

            }

            @Override
            public void onSaveExerciseEditDialog(ExerciseListModel exercise) {
                Exercise newExercise = new Exercise();
                newExercise.setName(exercise.getName());
                newExercise.setCategoryId(categoryId);
                newExercise.setId(exercise.getId());
                saveExercise(newExercise);
            }
        }, true, getData().get(position));

        dialog.show();
    }

    private void createExercise() {
        ExerciseEditDialog dialog = new ExerciseEditDialog(getActivity(), new ExerciseEditDialog.ExerciseEditDialogListener() {
            @Override
            public void onCancelExerciseEditDialog() {

            }

            @Override
            public void onSaveExerciseEditDialog(ExerciseListModel exercise) {
                Exercise newExercise = new Exercise();
                newExercise.setName(exercise.getName());
                newExercise.setCategoryId(categoryId);
                saveExercise(newExercise);
            }
        }, true, null);

        dialog.show();
    }

    private void removeExerciseAlert(final int position) {
        dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.dialog_exercise_remove_title))
                .setContentText(getString(R.string.dialog_exercise_remove_message))
                .setConfirmText(getString(R.string.yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        removeExercise(getData().get(position).getId());
                        dialog.cancel();
                    }
                })
                .setCancelText(getString(R.string.cancel))
                .showCancelButton(true);

        dialog.show();
    }

    private void addSet(final int position) {
        getNavigationManager().startWorkoutContainer(getData().get(position).getId());
    }
}
