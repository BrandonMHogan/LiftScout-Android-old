package com.brandonhogan.liftscout.presenters;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.SettingsHomeContract;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.utils.constants.DefaultScreens;
import com.brandonhogan.liftscout.utils.constants.TodayTransforms;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SettingsHomePresenter implements SettingsHomeContract.Presenter {

    // Injections
    //
    @Inject
    UserManager userManager;

    // Private Properties
    //
    private SettingsHomeContract.View view;

    @Inject
    public SettingsHomePresenter() {
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
    public void setView(SettingsHomeContract.View view) {
        this.view = view;
        init();
    }

    @Override
    public void onTransformSelected(int position) {
        userManager.setTransform(TodayTransforms.TRANSFORMS.get(position));
        view.saveSuccess(R.string.setting_home_transform_saved);
    }

    @Override
    public void onHomeDefaultSelected(int position) {
        userManager.setHomeDefault(DefaultScreens.SCREENS.get(position));
        view.saveSuccess(R.string.setting_home_screen_saved);
    }

    private void init() {
        setupHomeDefaults();
        setupTransforms();
    }

    private void setupHomeDefaults() {
        view.populateHomeDefaults(DefaultScreens.SCREENS, DefaultScreens.SCREENS.indexOf(userManager.getHomeDefaultValue()));
    }

    private void setupTransforms() {
        view.populateTransforms(TodayTransforms.TRANSFORMS, TodayTransforms.TRANSFORMS.indexOf(userManager.getTransformValue()));
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}
