package com.brandonhogan.liftscout.views.Intro.exercises;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;

import agency.tango.materialintroscreen.SlideFragment;
import agency.tango.materialintroscreen.parallax.ParallaxLinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Brandon on 2/27/2017.
 * Description :
 */

public class IntroExercisesSlideFragment extends SlideFragment implements IntroExercisesSlideContract.View {


    // Bindings
    //
    @Bind(R.id.create_button)
    AppCompatButton createButton;

    @Bind(R.id.image_button)
    ImageButton imageButton;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Bind(R.id.image_button_desc)
    TextView imageButtonDescription;

    @Bind(R.id.layout)
    ParallaxLinearLayout layout;

    // Private Properties
    //
    private IntroExercisesSlideContract.Presenter presenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_intro_exercises, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        presenter = new IntroExercisesSlidePresenter(this);
        presenter.viewCreated();
    }

    @Override
    public int backgroundColor() {
        return R.color.intro_slide_three;
    }

    @Override
    public int buttonsColor() {
        return R.color.theme_original_accent;
    }

    @Override
    public boolean canMoveFurther() {
        return true;
    }

    @OnClick(R.id.create_button)
    public void onCreateClick() {
        progressBar.setVisibility(View.VISIBLE);
        createButton.setEnabled(false);
        presenter.onButtonPressed();
    }

    @Override
    public void exercisesCreated(String value) {
        progressBar.setVisibility(View.GONE);

        Snackbar snackbar = Snackbar
                .make(layout, value, Snackbar.LENGTH_LONG);
        snackbar.show();

        createButton.setVisibility(View.GONE);
        imageButton.setVisibility(View.VISIBLE);
        imageButtonDescription.setVisibility(View.VISIBLE);
    }

    @Override
    public int getColor(int color) {
        return getResources().getColor(color);
    }

    @Override
    public String getStringValue(int value) {
        return getString(value);
    }
}
