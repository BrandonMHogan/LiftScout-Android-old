package com.brandonhogan.liftscout.presenters;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.SettingsHomeContract;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.utils.constants.DefaultScreens;
import com.brandonhogan.liftscout.utils.constants.TodayTransforms;

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
        userManager.setTransform(transforms.get(position));
        view.saveSuccess(R.string.setting_home_transform_saved);
    }

    @Override
    public void onHomeDefaultSelected(int position) {
        userManager.setHomeDefault(homeDefaults.get(position));
        view.saveSuccess(R.string.setting_home_screen_saved);
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
