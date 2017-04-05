package com.brandonhogan.liftscout.presenters;

import android.support.annotation.NonNull;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.SettingsProfileContract;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.utils.constants.Measurements;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SettingsProfilePresenter implements SettingsProfileContract.Presenter {

    // Injections
    //
    @Inject
    UserManager userManager;

    // Private Properties
    //
    private SettingsProfileContract.View view;
    private String originalMeasurementValue;
    private String currentSelectedMeasurement;
    private ArrayList<String> measurements;

    // Constructor
    //
    @Inject
    public SettingsProfilePresenter() {
        Injector.getAppComponent().inject(this);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void setView(@NonNull SettingsProfileContract.View view) {
        this.view = view;
        init();
    }

    @Override
    public void onMeasurementSelected(int position) {
        currentSelectedMeasurement = measurements.get(position);
        userManager.setMeasurement(currentSelectedMeasurement);
        view.saveSuccess(R.string.setting_profile_measurement_saved);
    }

    private void init() {
        measurements = new ArrayList<>();
        measurements.add(Measurements.POUNDS);
        measurements.add(Measurements.KILOGRAMS);

        originalMeasurementValue = userManager.getMeasurementValue();
        currentSelectedMeasurement = originalMeasurementValue;
        view.populateMeasurements(measurements, measurements.indexOf(originalMeasurementValue));
    }

    // Exists mostly for the unit tests
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}