package com.brandonhogan.liftscout.views.settings.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by Brandon on 2/17/2017.
 * Description :
 */

public class SettingsProfileFragment extends BaseFragment implements SettingsProfileContract.View {


    // Instance
    //
    public static SettingsProfileFragment newInstance() {
        return new SettingsProfileFragment();
    }


    // Private Properties
    //
    private SettingsProfileContract.Presenter presenter;
    private View rootView;


    // Bindings
    //
    @Bind(R.id.measurementSpinner)
    MaterialSpinner measurementSpinner;


    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_settings_profile, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new SettingsProfilePresenter(this);

        setTitle(getResources().getString(R.string.title_frag_settings_profile));

        presenter.viewCreated();
    }

    @Override
    public void saveSuccess(int message) {
        Toast.makeText(getActivity(), getString(message), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void populateMeasurements(ArrayList<String> measurements, int position) {
        measurementSpinner.setItems(measurements);
        measurementSpinner.setSelectedIndex(position);

        measurementSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                presenter.onMeasurementSelected(position);
            }
        });
    }
}
