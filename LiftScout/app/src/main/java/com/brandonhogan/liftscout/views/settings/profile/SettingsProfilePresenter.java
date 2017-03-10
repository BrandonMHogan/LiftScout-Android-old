package com.brandonhogan.liftscout.views.settings.profile;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.constants.Measurements;
import com.brandonhogan.liftscout.core.managers.UserManager;
import com.brandonhogan.liftscout.injection.components.Injector;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by Brandon on 2/17/2017.
 * Description :
 */

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
    public SettingsProfilePresenter(SettingsProfileContract.View view) {
        Injector.getAppComponent().inject(this);
        this.view = view;

        originalMeasurementValue = userManager.getMeasurementValue();
    }

    // Contract
    //
    @Override
    public void viewCreated() {
        measurements = new ArrayList<>();
        measurements.add(Measurements.POUNDS);
        measurements.add(Measurements.KILOGRAMS);

        currentSelectedMeasurement = originalMeasurementValue;
        view.populateMeasurements(measurements, measurements.indexOf(originalMeasurementValue));
    }

    @Override
    public void onMeasurementSelected(int position) {
        currentSelectedMeasurement = measurements.get(position);
        userManager.setMeasurement(currentSelectedMeasurement);
        view.saveSuccess(R.string.setting_profile_measurement_saved);
    }
}