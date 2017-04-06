package com.brandonhogan.liftscout.presenters;

import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.IntroSettingsContract;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.utils.constants.DefaultScreens;
import com.brandonhogan.liftscout.utils.constants.Measurements;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Brandon on 2/26/2017.
 * Description :
 */

@Singleton
public class IntroSettingsPresenter implements IntroSettingsContract.Presenter {

    // Injections
    //
    @Inject
    UserManager userManager;

    // Private Properties
    //
    private IntroSettingsContract.View view;

    @Inject
    public IntroSettingsPresenter() {
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
    public void setView(IntroSettingsContract.View view) {
        this.view = view;
        init();
    }

    @Override
    public void onHomeDefaultSelected(int position) {
        userManager.setHomeDefault(DefaultScreens.SCREENS.get(position));
    }

    @Override
    public void onMeasurementDefaultSelected(int position) {
        userManager.setMeasurement(Measurements.MEASUREMENTS.get(position));
    }

    private void init() {
        setupHomeDefaults();
        setupMeasurementDefaults();
    }

    private void setupHomeDefaults() {
        view.populateHomeDefaults(DefaultScreens.SCREENS, DefaultScreens.SCREENS.indexOf(userManager.getHomeDefaultValue()));
    }

    private void setupMeasurementDefaults() {
        view.populateMeasurementDefaults(Measurements.MEASUREMENTS, Measurements.MEASUREMENTS.indexOf(userManager.getMeasurementValue()));
    }
}
