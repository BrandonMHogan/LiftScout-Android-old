package com.brandonhogan.liftscout.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.SettingsHomeContract;
import com.brandonhogan.liftscout.presenters.SettingsHomePresenter;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;

public class SettingsHomeFragment extends BaseFragment implements SettingsHomeContract.View {

    @Inject SettingsHomeContract.Presenter presenter;

    // Instance
    //
    public static SettingsHomeFragment newInstance() {
        return new SettingsHomeFragment();
    }

    // Bindings
    //
    @Bind(R.id.homeDefaultSpinner)
    MaterialSpinner homeDefaultSpinner;

    @Bind(R.id.todayTransformSpinner)
    MaterialSpinner transformSpinner;

    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_settings_home, container, false);
        Injector.getFragmentComponent().inject(this);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.setView(this);
        setTitle(getResources().getString(R.string.title_frag_settings_home));
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

    // Contract
    //
    @Override
    public void populateTransforms(ArrayList<String> themes, int position) {
        transformSpinner.setItems(themes);
        transformSpinner.setSelectedIndex(position);

        transformSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                presenter.onTransformSelected(position);
            }
        });
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
    public void saveSuccess(int message) {
        Toast.makeText(getActivity(), getString(message), Toast.LENGTH_SHORT).show();
    }
}
