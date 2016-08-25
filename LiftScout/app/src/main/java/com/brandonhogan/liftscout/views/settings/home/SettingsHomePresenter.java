package com.brandonhogan.liftscout.views.settings.home;

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
    private String currentTransformValue;

    public SettingsHomePresenter(SettingsHomeContract.View view) {
        Injector.getAppComponent().inject(this);
        this.view = view;
    }


    // Contract
    //
    @Override
    public void viewCreated() {
        transforms = new ArrayList<>();

        transforms.add(TodayTransforms.DEFAULT);
        transforms.add(TodayTransforms.ACCORDION);
        transforms.add(TodayTransforms.DEPTH_PAGE);
        transforms.add(TodayTransforms.FOREGROUND_TO_BACKGROUND);
        transforms.add(TodayTransforms.ROTATE_DOWN);
        transforms.add(TodayTransforms.ROTATE_UP);
        transforms.add(TodayTransforms.SCALE_IN_OUT);
        transforms.add(TodayTransforms.STACK);
        transforms.add(TodayTransforms.ZOOM_IN);
        transforms.add(TodayTransforms.ZOOM_OUT);
        transforms.add(TodayTransforms.ZOOM_OUT_SLIDE);

        view.populateTransforms(transforms, transforms.indexOf(userManager.getTransformValue()));
    }

    @Override
    public void onTransformSelected(int position) {
        currentTransformValue = transforms.get(position);
    }

    @Override
    public void onSave() {
        userManager.setTransform(currentTransformValue);
        view.saveSuccess();
    }
}
