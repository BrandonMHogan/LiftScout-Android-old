package com.brandonhogan.liftscout.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.fragments.base.BHDetailFragment;
import com.brandonhogan.liftscout.fragments.base.BHFragment;

public class ExerciseTypeList extends BHDetailFragment {

    public ExerciseTypeList() {
        super("Exercises");
    }

    @Override
    public ApplicationArea applicationArea() {
        return ApplicationArea.EXERCISES;
    }

    @Override
    public BHFragment parentFragment() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_exercise_type_list, container, false);

        return view;
    }
}
