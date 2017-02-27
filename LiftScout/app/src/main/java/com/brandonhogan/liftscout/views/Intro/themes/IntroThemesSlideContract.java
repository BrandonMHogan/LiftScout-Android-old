package com.brandonhogan.liftscout.views.Intro.themes;

import java.util.ArrayList;

/**
 * Created by Brandon on 2/27/2017.
 * Description :
 */

public class IntroThemesSlideContract {
    interface View {
        void populateThemes(ArrayList<String> screens, int position);
        void themeSelected(int theme);
    }

    interface Presenter {
        void viewCreated();
        void onThemeSelected(int position);
    }
}
