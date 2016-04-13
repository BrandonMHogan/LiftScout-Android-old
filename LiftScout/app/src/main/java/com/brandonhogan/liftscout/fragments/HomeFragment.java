package com.brandonhogan.liftscout.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.AppController;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.fragments.base.BHFragment;

public class HomeFragment extends BHFragment {

    public HomeFragment() {
        super(AppController.getInstance().getString(R.string.nav_home));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);

        //FAB button
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        return view;
    }
}
