package com.brandonhogan.liftscout.interfaces.contracts;

import com.brandonhogan.liftscout.presenters.base.PresenterBase;

import java.util.ArrayList;

public interface IntroThemesContract {
    interface View {
        void populateThemes(ArrayList<String> screens, int position);
        void themeSelected(int theme);
    }

    interface Presenter extends PresenterBase {
        void setView(IntroThemesContract.View view);
        void onThemeSelected(int position);
    }
}
