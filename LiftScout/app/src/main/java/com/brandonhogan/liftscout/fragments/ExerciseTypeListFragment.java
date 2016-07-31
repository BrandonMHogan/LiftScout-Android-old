package com.brandonhogan.liftscout.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.fragments.base.BaseFragment;

public class ExerciseTypeListFragment extends BaseFragment {

    // Instance
    //
    public static ExerciseTypeListFragment newInstance() {
        return new ExerciseTypeListFragment();
    }


    // Private Properties
    //
    private View rootView;


    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_exercise_type_list, null);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(getResources().getString(R.string.title_activity_exercise_type_list));

    }
}
