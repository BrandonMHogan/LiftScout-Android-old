package com.brandonhogan.liftscout.views.settings.display;

import java.util.ArrayList;

public interface SettingsDisplayContract {

    interface View {
        void populateThemes(ArrayList<String> themes, int position);
        void saveSuccess(boolean restart);
        void showAlert();
    }

    interface Presenter {
        void viewCreated();
        void onThemeSelected(int position);
        void changeTheme();
        int getOriginalThemeIndex();
    }

}
