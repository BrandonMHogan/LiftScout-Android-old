package com.brandonhogan.liftscout.views.Intro.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.utils.AttrUtil;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import agency.tango.materialintroscreen.SlideFragment;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brandon on 2/26/2017.
 * Description :
 */

public class IntroSettingsSlideFragment extends SlideFragment implements IntroSettingsSlideContract.View {


    // Bindings
    //
    @Bind(R.id.homeDefaultSpinner)
    MaterialSpinner homeDefaultSpinner;

    @Bind(R.id.measurementSpinner)
    MaterialSpinner measurementSpinner;


    // Private Properties
    //
    private IntroSettingsSlideContract.Presenter presenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_intro_settings, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        homeDefaultSpinner.setBackgroundResource(AttrUtil.getAttributeRes(getActivity().getTheme(), R.attr.colorFill));
        measurementSpinner.setBackgroundResource(AttrUtil.getAttributeRes(getActivity().getTheme(), R.attr.colorFill));

        presenter = new IntroSettingsSlidePresenter(this);
        presenter.viewCreated();
    }

    @Override
    public int backgroundColor() {
        return R.color.intro_slide_two;
    }

    @Override
    public int buttonsColor() {
        return R.color.theme_original_accent;
    }

    @Override
    public boolean canMoveFurther() {
        return true;
    }


    @Override
    public void populateHomeDefaults(ArrayList<String> screens, int position) {
        homeDefaultSpinner.setItems(screens);
        homeDefaultSpinner.setSelectedIndex(position);

        homeDefaultSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                presenter.onHomeDefaultSelected(position);
            }
        });
    }

    @Override
    public void populateMeasurementDefaults(ArrayList<String> screens, int position) {
        measurementSpinner.setItems(screens);
        measurementSpinner.setSelectedIndex(position);

        measurementSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                presenter.onMeasurementDefaultSelected(position);
            }
        });
    }
}
