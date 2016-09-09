package com.brandonhogan.liftscout.views.settings.display;

import android.content.Context;

import com.brandonhogan.liftscout.core.constants.Themes;
import com.brandonhogan.liftscout.core.managers.UserManager;
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


    // Private Functions
    //
    private void saveSettings(boolean restartNeeded) {
        userManager.setTheme(currentSelectedTheme);

        view.saveSuccess(restartNeeded);
    }


    // Contract
    //
    @Override
    public void viewCreated() {

        themes = new ArrayList<>();
        themes.add(Themes.DARK);
        themes.add(Themes.LIGHT);
        themes.add(Themes.GREEN_DARK);
        themes.add(Themes.PURPLE_DARK);

        currentSelectedTheme = originalThemeValue;
        view.populateThemes(themes, themes.indexOf(originalThemeValue));
    }

    @Override
    public void onSave(boolean validated) {

        boolean valueChanged = !originalThemeValue.equals(currentSelectedTheme);

        if (!validated && valueChanged)
            view.showAlert();
        else
            saveSettings(valueChanged);
    }

    @Override
    public void onThemeSelected(int position) {
        currentSelectedTheme = themes.get(position);
    }
}