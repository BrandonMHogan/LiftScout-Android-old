package com.brandonhogan.liftscout.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.interfaces.contracts.GraphContract;
import com.brandonhogan.liftscout.utils.controls.graphs.line.MyLineGraph;
import com.brandonhogan.liftscout.presenters.WorkoutGraphPresenter;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.brandonhogan.liftscout.events.TrackerEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;

public class GraphFragment extends BaseFragment implements GraphContract.View {

    // Static Properties
    //
    private static final String BUNDLE_SELECTED_EXERCISE_ID = "selectedExerciseIdBundle";
    private static final String BUNDLE_SELECTED_EXERCISE_NAME = "selectedExerciseNameBundle";
    private static final String BUNDLE_SELECTED_DATE_RANGE = "selectedDateRangeBundle";
    private static final String BUNDLE_SELECTED_GRAPH_TYPE = "selectedGraphTypeBundle";
    private static final String BUNDLE_EXERCISE_ID = "exerciseIdBundle";


    // Bindings
    //
    @Bind(R.id.my_line_graph)
    MyLineGraph graph;


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

        presenter = new WorkoutGraphPresenter(this, getArguments().getInt(BUNDLE_EXERCISE_ID));
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        graph.init(getActivity().getTheme(), false);
        presenter.viewCreated();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(saveState != null) {
            graph.setExercise(
                    saveState.getInt(BUNDLE_SELECTED_EXERCISE_ID),
                    saveState.getString(BUNDLE_SELECTED_EXERCISE_NAME),
                    saveState.getInt(BUNDLE_SELECTED_DATE_RANGE),
                    saveState.getInt(BUNDLE_SELECTED_GRAPH_TYPE));
            saveState = null;
        }
    }

    @Override
    protected Bundle saveState() {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_SELECTED_EXERCISE_ID, graph.getExerciseId());
        bundle.putString(BUNDLE_SELECTED_EXERCISE_NAME, graph.getExerciseName());
        bundle.putInt(BUNDLE_SELECTED_DATE_RANGE, graph.getCurrentRangePosition());
        bundle.putInt(BUNDLE_SELECTED_GRAPH_TYPE, graph.getCurrentGraphType());
        return bundle;
    }

    @Override
    public void onStop() {
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
