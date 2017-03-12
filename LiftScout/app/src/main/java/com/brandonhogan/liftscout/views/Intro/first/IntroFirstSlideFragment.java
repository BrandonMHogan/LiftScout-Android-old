package com.brandonhogan.liftscout.views.Intro.first;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;

import agency.tango.materialintroscreen.SlideFragment;

/**
 * Created by Brandon on 2/27/2017.
 * Description :
 */

public class IntroFirstSlideFragment extends SlideFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_intro_first, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int backgroundColor() {
        return R.color.intro_slide_one;
    }

    @Override
    public int buttonsColor() {
        return R.color.green_transparent_05;
    }

    @Override
    public boolean canMoveFurther() {
        return true;
    }

}
