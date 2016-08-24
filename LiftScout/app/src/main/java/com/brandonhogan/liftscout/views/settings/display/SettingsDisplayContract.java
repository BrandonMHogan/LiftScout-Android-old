package com.brandonhogan.liftscout.views.settings.display;

import com.brandonhogan.liftscout.core.model.UserSetting;

import java.util.ArrayList;

public interface SettingsDisplayContract {

    interface View {
        void populateThemes(ArrayList<String> themes);
    }

    interface UserActionListener {
        void loadThemes();
        void currentTheme(UserSetting theme);

        void onThemeSelected(int position);
        void onSave();
    }

}
