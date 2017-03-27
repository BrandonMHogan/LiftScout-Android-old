package com.brandonhogan.liftscout.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.BuildConfig;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.interfaces.contracts.ExerciseDetailContract;
import com.brandonhogan.liftscout.presenters.ExerciseDetailPresenter;
import com.brandonhogan.liftscout.views.base.BaseFragment;

/**
 * Created by Brandon on 3/27/2017.
 * Description :
 */

public class ExerciseDetailFragment extends BaseFragment implements ExerciseDetailContract.View {


    // Private Static Properties
    //
    private final static String BUNDLE_EXERCISE_ID = "exerciseIdBundle";
    private final static String BUNDLE_NEW_EXERCISE = "newExerciseBundle";


    // Instance
    //
    public static ExerciseDetailFragment newInstance(int exerciseId) {
        Bundle args = new Bundle();
        args.putInt(BUNDLE_EXERCISE_ID, exerciseId);
        args.putBoolean(BUNDLE_NEW_EXERCISE, false);

        ExerciseDetailFragment fragment = new ExerciseDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public static ExerciseDetailFragment newInstance() {
        Bundle args = new Bundle();
        args.putInt(BUNDLE_EXERCISE_ID, 0);
        args.putBoolean(BUNDLE_NEW_EXERCISE, true);

        ExerciseDetailFragment fragment = new ExerciseDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }


    // Private Properties
    //
    private View rootView;
    private ExerciseDetailContract.Presenter presenter;


    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_exercise_detail, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new ExerciseDetailPresenter(this, this.getArguments().getBoolean(BUNDLE_NEW_EXERCISE), this.getArguments().getInt(BUNDLE_EXERCISE_ID));

        setTitle(getString(R.string.exercise_new));
        presenter.viewCreated();

    }
}
