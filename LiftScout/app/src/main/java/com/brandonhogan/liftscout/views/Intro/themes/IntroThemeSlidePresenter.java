package com.brandonhogan.liftscout.views.Intro.themes;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.constants.Themes;
import com.brandonhogan.liftscout.core.managers.UserManager;
import com.brandonhogan.liftscout.injection.components.Injector;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by Brandon on 2/27/2017.
 * Description :
 */

public class IntroThemeSlidePresenter implements IntroThemesSlideContract.Presenter {


    // Injections
    //
    @Inject
    UserManager userManager;


    // Private Properties
    //
    private IntroThemesSlideContract.View view;
    private ArrayList<String> themes;
    private String currentTheme;

    public IntroThemeSlidePresenter(IntroThemesSlideContract.View view) {
        Injector.getAppComponent().inject(this);
        this.view = view;
    }


    // Contract
    //
    @Override
    public void viewCreated() {
        setupThemes();
    }

    @Override
    public void onThemeSelected(int position) {
        currentTheme = themes.get(position);
        userManager.setTheme(currentTheme);

        switch (currentTheme) {
            case Themes.DARK:
                view.themeSelected(R.style.AppTheme_Dark);
                return;
            case Themes.GREEN_DARK:
                view.themeSelected(R.style.AppTheme_Green_Dark);
                return;
            case Themes.PURPLE_DARK:
                view.themeSelected(R.style.AppTheme_Purple_Dark);
                return;
            case Themes.LIGHT:
                view.themeSelected(R.style.AppTheme_Light);
        }
    }


    // Private Functions
    //

    private void setupThemes() {
        themes = new ArrayList<>();

        themes.add(Themes.DARK);
        themes.add(Themes.LIGHT);
        themes.add(Themes.GREEN_DARK);
        themes.add(Themes.PURPLE_DARK);

        view.populateThemes(themes, themes.indexOf(userManager.getThemeValue()));
    }
}
