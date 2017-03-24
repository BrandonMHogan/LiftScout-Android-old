package com.brandonhogan.liftscout.presenters;

import com.brandonhogan.liftscout.interfaces.contracts.IntroSettingsContract;
import com.brandonhogan.liftscout.utils.constants.DefaultScreens;
import com.brandonhogan.liftscout.utils.constants.Measurements;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.injection.components.Injector;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by Brandon on 2/26/2017.
 * Description :
 */

public class IntroSettingsPresenter implements IntroSettingsContract.Presenter {


    // Injections
    //
    @Inject
    UserManager userManager;


    // Private Properties
    //
    private IntroSettingsContract.View view;
    private ArrayList<String> homeDefaults;
    private ArrayList<String> measurements;
    private String currentHomeDefaultValue;
    private String currentSelectedMeasurement;

    public IntroSettingsPresenter(IntroSettingsContract.View view) {
        Injector.getAppComponent().inject(this);
        this.view = view;
    }


    // Contract
    //
    @Override
    public void viewCreated() {
        setupHomeDefaults();
        setupMeasurementDefaults();
    }

    @Override
    public void onHomeDefaultSelected(int position) {
        currentHomeDefaultValue = homeDefaults.get(position);
        userManager.setHomeDefault(currentHomeDefaultValue);
    }

    @Override
    public void onMeasurementDefaultSelected(int position) {
        currentSelectedMeasurement = measurements.get(position);
        userManager.setMeasurement(currentSelectedMeasurement);
    }


    // Private Functions
    //

    private void setupHomeDefaults() {
        homeDefaults = new ArrayList<>();

        homeDefaults.add(DefaultScreens.TODAY);
        homeDefaults.add(DefaultScreens.CALENDAR);

        view.populateHomeDefaults(homeDefaults, homeDefaults.indexOf(userManager.getHomeDefaultValue()));
    }

    private void setupMeasurementDefaults() {

        measurements = new ArrayList<>();
        measurements.add(Measurements.POUNDS);
        measurements.add(Measurements.KILOGRAMS);

        view.populateMeasurementDefaults(measurements, measurements.indexOf(userManager.getMeasurementValue()));
    }
}
