package com.brandonhogan.liftscout.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.IntroSettingsContract;
import com.brandonhogan.liftscout.utils.AttrUtil;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import javax.inject.Inject;

import agency.tango.materialintroscreen.SlideFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IntroSettingsFragment extends SlideFragment implements IntroSettingsContract.View {

    // Injections
    //
    @Inject
    IntroSettingsContract.Presenter presenter;

    // Bindings
    //
    @BindView(R.id.homeDefaultSpinner)
    MaterialSpinner homeDefaultSpinner;

    @BindView(R.id.measurementSpinner)
    MaterialSpinner measurementSpinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_intro_settings, container, false);
        Injector.getFragmentComponent().inject(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter.setView(this);

        homeDefaultSpinner.setBackgroundResource(AttrUtil.getAttributeRes(getActivity().getTheme(), R.attr.colorFill));
        measurementSpinner.setBackgroundResource(AttrUtil.getAttributeRes(getActivity().getTheme(), R.attr.colorFill));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }

    @Override
    public int backgroundColor() {
        return R.color.light_theme_background;
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
