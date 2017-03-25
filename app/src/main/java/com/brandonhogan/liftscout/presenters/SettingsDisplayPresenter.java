package com.brandonhogan.liftscout.presenters;

import android.content.Context;

import com.brandonhogan.liftscout.interfaces.contracts.SettingsDisplayContract;
import com.brandonhogan.liftscout.utils.constants.Themes;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.injection.components.Injector;

import java.util.ArrayList;

import javax.inject.Inject;

public class SettingsDisplayPresenter implements SettingsDisplayContract.Presenter {


    // Injections
    //
    @Inject
    UserManager userManager;

    @Inject
    Context context;


    // Private Properties
    //
    private SettingsDisplayContract.View view;
    private String originalThemeValue;
    private String currentSelectedTheme;
    private ArrayList<String> themes;


    // Constructor
    //
    public SettingsDisplayPresenter(SettingsDisplayContract.View view) {
        Injector.getAppComponent().inject(this);
        this.view = view;

        originalThemeValue = userManager.getThemeValue();
    }

    // Contract
    //
    @Override
    public void viewCreated() {
        themes = Themes.THEMES;
        currentSelectedTheme = originalThemeValue;
        view.populateThemes(themes, themes.indexOf(originalThemeValue));
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
}