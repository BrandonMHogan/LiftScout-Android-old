package com.brandonhogan.liftscout.views.graphs.exercises;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.controls.graphs.line.MyLineGraph;
import com.brandonhogan.liftscout.core.managers.GraphManager;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.views.base.BaseFragment;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Brandon on 2/16/2017.
 * Description :
 */

public class GraphExercisesFragment extends BaseFragment implements GraphExercisesContract.View {

    @Inject
    GraphManager graphManager;

    @Bind(R.id.my_button)
    Button myButton;

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

        lineGraph.init(getActivity().getTheme());
    }

    @Override
    public void onResume() {
        super.onResume();

        getNavigationManager().unlockDrawer();
        presenter.onResume();
    }

    @Override
    public void setSelectedExercise(int id, String name) {
        myButton.setText(name);
        lineGraph.setExercise(id, name);
    }

    @OnClick(R.id.my_button)
    void onClick() {
        graphManager.setInSearch(true);
        graphManager.setGraphName(getNavigationManager().getCurrentFragmentName());
        getNavigationManager().lockDrawer();
        getNavigationManager().startCategoryListGraphSearch();
    }
}
