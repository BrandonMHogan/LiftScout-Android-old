package com.brandonhogan.liftscout.fragments.workout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.fragments.base.BaseFragment;

public class WorkoutHistoryFragment extends BaseFragment {


    // Instance
    //
    public static WorkoutHistoryFragment newInstance(long progressId)
    {
        WorkoutHistoryFragment frag = new WorkoutHistoryFragment();
        Bundle bundle = new Bundle();

        bundle.putLong(BUNDLE_PROGRESS_ID, progressId);
        frag.setArguments(bundle);

        return frag;
    }


    // Private Properties
    //
    private View rootView;


    // Static Properties
    //
    private static final String BUNDLE_PROGRESS_ID = "progressIdBundle";


    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_workout_history, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
