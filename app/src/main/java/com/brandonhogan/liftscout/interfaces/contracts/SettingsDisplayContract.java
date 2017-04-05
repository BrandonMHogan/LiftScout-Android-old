package com.brandonhogan.liftscout.interfaces.contracts;

import com.brandonhogan.liftscout.presenters.base.PresenterBase;

import java.util.ArrayList;

public interface SettingsDisplayContract {

    interface View {
        void populateThemes(ArrayList<String> themes, int position);
        void saveSuccess(boolean restart);
        void showAlert();
    }

    interface Presenter extends PresenterBase {
        void setView(SettingsDisplayContract.View view);
        void onThemeSelected(int position);
        void changeTheme();
        int getOriginalThemeIndex();
    }

}
