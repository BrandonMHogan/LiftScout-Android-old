package com.brandonhogan.liftscout.views.graphs.exercises;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.views.base.BaseFragment;

/**
 * Created by Brandon on 2/16/2017.
 * Description :
 */

public class ExercisesFragment extends BaseFragment {

    // Instance
    //
    public static ExercisesFragment newInstance()
    {
        ExercisesFragment frag = new ExercisesFragment();
        return frag;
    }


    // Private Properties
    //
    private View rootView;


    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_graphs_exercises, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
