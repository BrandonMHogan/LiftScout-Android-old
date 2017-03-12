package com.brandonhogan.liftscout.views.graphs.exercises;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.controls.graphs.line.MyLineGraph;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.views.base.BaseFragment;

import butterknife.Bind;

/**
 * Created by Brandon on 2/16/2017.
 * Description :
 */

public class GraphExercisesFragment extends BaseFragment implements GraphExercisesContract.View {

    @Bind(R.id.my_line_graph)
    MyLineGraph lineGraph;

    // Instance
    //
    public static GraphExercisesFragment newInstance()
    {
        GraphExercisesFragment frag = new GraphExercisesFragment();
        return frag;
    }

    // Private Properties
    //
    private View rootView;
    private GraphExercisesContract.Presenter presenter;


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
        Injector.getAppComponent().inject(this);

        presenter = new GraphExercisesPresenter(this);
        presenter.viewCreated();

        lineGraph.init(getActivity().getTheme(), true);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void setSelectedExercise(int id, String name) {
        lineGraph.setExercise(id, name);
    }
}
