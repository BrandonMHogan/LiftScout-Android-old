package com.brandonhogan.liftscout.views.graphs.categories;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.brandonhogan.liftscout.views.workout.graph.GraphFragment;

/**
 * Created by Brandon on 2/16/2017.
 * Description :
 */

public class CategoriesFragment extends BaseFragment {


    // Instance
    //
    public static CategoriesFragment newInstance()
    {
        CategoriesFragment frag = new CategoriesFragment();
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
        rootView = inflater.inflate(R.layout.frag_graphs_categories, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
