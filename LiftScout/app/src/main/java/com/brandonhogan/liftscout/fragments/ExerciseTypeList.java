package com.brandonhogan.liftscout.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.fragments.base.BHFragment;

public class ExerciseTypeList extends BHFragment {

    public ExerciseTypeList() {
        super("Exercises");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_exercise_type_list, container, false);

        return view;
    }
}
