package com.brandonhogan.liftscout.views.settings.display;

import android.content.Context;

import com.brandonhogan.liftscout.core.constants.Themes;
import com.brandonhogan.liftscout.core.managers.UserManager;
import com.brandonhogan.liftscout.injection.components.Injector;

import java.util.ArrayList;

import javax.inject.Inject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SettingsDisplayPresenter implements SettingsDisplayContract.Presenter {


    // Injections
    //
    @Inject
    UserManager userManager;

    @Inject
    Context context;


    // Private Properties
    //
    private SettingsDisplayContract.View mView;
    private String originalThemeValue;
    private String currentSelectedTheme;
    private ArrayList<String> themes;
    private SweetAlertDialog dialog;


    // Constructor
    //
    public SettingsDisplayPresenter(SettingsDisplayContract.View view) {
        Injector.getAppComponent().inject(this);
        mView = view;

        originalThemeValue = userManager.getTheme().getValue();
    }


    // Private Functions
    //
    private void saveSettings(boolean restartNeeded) {
        userManager.setTheme(currentSelectedTheme);

        mView.saveSuccess(restartNeeded);
    }


    // Contract
    //
    @Override
    public void loadThemes() {

        themes = new ArrayList<>();
        themes.add(Themes.DARK);
        themes.add(Themes.LIGHT);

        mView.populateThemes(themes, themes.indexOf(originalThemeValue));
    }

    @Override
    public void onSave(boolean validated) {

        boolean valueChanged = !originalThemeValue.equals(currentSelectedTheme);

        if (!validated && valueChanged)
            mView.showAlert();
        else
            saveSettings(valueChanged);
    }

    @Override
    public void onThemeSelected(int position) {
        currentSelectedTheme = themes.get(position);
    }
}