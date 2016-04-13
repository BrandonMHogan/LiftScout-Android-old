package com.brandonhogan.liftscout.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.fragments.base.BHFragment;

public class InitFragment extends BHFragment {

    public InitFragment() {
        super("Lift Scout Setup");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_init, container, false);

        return view;
    }
}
