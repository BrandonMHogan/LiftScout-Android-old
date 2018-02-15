package com.brandonhogan.liftscout.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.SettingsProfileContract;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Brandon on 2/17/2017.
 * Description :
 */

public class SettingsProfileFragment extends BaseFragment implements SettingsProfileContract.View {

    // Injections
    //
    @Inject SettingsProfileContract.Presenter presenter;

    // Bindings
    //
    @BindView(R.id.measurementSpinner)
    MaterialSpinner measurementSpinner;

    // Instance
    //
    public static SettingsProfileFragment newInstance() {
        return new SettingsProfileFragment();
    }

    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_settings_profile, container, false);
        Injector.getFragmentComponent().inject(this);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.setView(this);
        setTitle(getResources().getString(R.string.title_frag_settings_profile));
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
