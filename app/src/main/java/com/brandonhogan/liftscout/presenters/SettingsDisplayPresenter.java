package com.brandonhogan.liftscout.presenters;

import androidx.annotation.NonNull;

import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.SettingsDisplayContract;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.utils.constants.Themes;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SettingsDisplayPresenter implements SettingsDisplayContract.Presenter {

    // Injections
    //
    @Inject
    UserManager userManager;

    // Private Properties
    //
    private SettingsDisplayContract.View view;
    private String originalThemeValue;
    private String currentSelectedTheme;
    private ArrayList<String> themes;

    // Constructor
    //
    @Inject
    public SettingsDisplayPresenter() {
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
    public void setView(@NonNull SettingsDisplayContract.View view) {
        this.view = view;
        init();
    }

    @Override
    public void changeTheme() {
        userManager.setTheme(currentSelectedTheme);
        view.saveSuccess(true);
    }

    @Override
    public void onThemeSelected(int position) {
        currentSelectedTheme = themes.get(position);

        boolean valueChanged = !originalThemeValue.equals(currentSelectedTheme);

        if (valueChanged)
            view.showAlert();
    }

    @Override
    public int getOriginalThemeIndex() {
        currentSelectedTheme = originalThemeValue;
        return themes.indexOf(originalThemeValue);
    }

    private void init() {
        themes = Themes.THEMES;

        originalThemeValue = userManager.getThemeValue();
        currentSelectedTheme = originalThemeValue;
        view.populateThemes(themes, themes.indexOf(originalThemeValue));
    }


    // Exists mostly for the unit tests
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}