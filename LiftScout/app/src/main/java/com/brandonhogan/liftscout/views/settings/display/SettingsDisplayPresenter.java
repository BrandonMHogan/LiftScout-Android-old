package com.brandonhogan.liftscout.views.settings.display;

import com.brandonhogan.liftscout.core.constants.Themes;
import com.brandonhogan.liftscout.core.model.UserSetting;

import java.util.ArrayList;

public class SettingsDisplayPresenter implements SettingsDisplayContract.UserActionListener {

    SettingsDisplayContract.View mView;

    public SettingsDisplayPresenter(SettingsDisplayContract.View view) {
        mView = view;
    }

    @Override
    public void currentTheme(UserSetting theme) {

    }

    @Override
    public void loadThemes() {

        ArrayList<String> themes = new ArrayList<>();
        themes.add(Themes.DARK);
        themes.add(Themes.LIGHT);

        mView.populateThemes(themes);
    }

    @Override
    public void onSave() {

    }

    @Override
    public void onThemeSelected(int position) {

    }
}