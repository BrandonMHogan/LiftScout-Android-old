package com.brandonhogan.liftscout.views.settings.home;

import com.brandonhogan.liftscout.core.constants.DefaultScreens;
import com.brandonhogan.liftscout.core.constants.TodayTransforms;
import com.brandonhogan.liftscout.core.managers.UserManager;
import com.brandonhogan.liftscout.injection.components.Injector;

import java.util.ArrayList;

import javax.inject.Inject;

public class SettingsHomePresenter implements SettingsHomeContract.Presenter {


    // Injections
    //
    @Inject
    UserManager userManager;


    // Private Properties
    //
    private SettingsHomeContract.View view;
    private ArrayList<String> transforms;
    private ArrayList<String> homeDefaults;
    private String currentTransformValue;
    private String currentHomeDefaultValue;

    public SettingsHomePresenter(SettingsHomeContract.View view) {
        Injector.getAppComponent().inject(this);
        this.view = view;
    }


    // Contract
    //
    @Override
    public void viewCreated() {
        setupHomeDefaults();
        setupTransforms();
    }

    @Override
    public void onTransformSelected(int position) {
        currentTransformValue = transforms.get(position);
    }

    @Override
    public void onHomeDefaultSelected(int position) {
        currentHomeDefaultValue = homeDefaults.get(position);
    }

    @Override
    public void onSave() {
        userManager.setTransform(currentTransformValue);
        userManager.setHomeDefault(currentHomeDefaultValue);

        view.saveSuccess();
    }

    // Private Functions
    //

    private void setupHomeDefaults() {
        homeDefaults = new ArrayList<>();

        homeDefaults.add(DefaultScreens.TODAY);
        homeDefaults.add(DefaultScreens.CALENDAR);

        view.populateHomeDefaults(homeDefaults, homeDefaults.indexOf(userManager.getHomeDefaultValue()));
    }

    private void setupTransforms() {
        transforms = new ArrayList<>();

        transforms.add(TodayTransforms.DEFAULT);
        transforms.add(TodayTransforms.OVERSHOOT);
        transforms.add(TodayTransforms.FAST_OUT_LINEAR_IN);
        transforms.add(TodayTransforms.BOUNCE);
        transforms.add(TodayTransforms.ACCELERATE_DECELERATE);

        view.populateTransforms(transforms, transforms.indexOf(userManager.getTransformValue()));
    }
}
