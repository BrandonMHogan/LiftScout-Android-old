package com.brandonhogan.liftscout.views.workout.graph;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.controls.graphs.line.MyLineGraph;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.brandonhogan.liftscout.views.workout.TrackerEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;

public class GraphFragment extends BaseFragment implements GraphContract.View {

    @Bind(R.id.my_line_graph)
    MyLineGraph graph;

    // Static Properties
    //
    private static final String BUNDLE_EXERCISE_ID = "exerciseIdBundle";


    // Instance
    //
    public static GraphFragment newInstance(int exerciseId)
    {
        GraphFragment frag = new GraphFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(BUNDLE_EXERCISE_ID, exerciseId);
        frag.setArguments(bundle);

        return frag;
    }


    // Private Properties
    //
    private View rootView;
    private GraphContract.Presenter presenter;


    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_workout_graph, container, false);

        presenter = new GraphPresenter(this, getArguments().getInt(BUNDLE_EXERCISE_ID));
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        graph.init(getActivity().getTheme());
        presenter.viewCreated();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void setupGraph(int exerciseId, String name) {
        graph.setExercise(exerciseId, name);
    }


    // Subscriptions
    //

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTrackerEvent(TrackerEvent event) {
        if (event.isUpdated)
            graph.update(true);
    }
}
