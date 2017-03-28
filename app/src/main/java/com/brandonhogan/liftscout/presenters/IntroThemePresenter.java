package com.brandonhogan.liftscout.presenters;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.IntroThemesContract;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.utils.constants.Themes;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by Brandon on 2/27/2017.
 * Description :
 */

public class IntroThemePresenter implements IntroThemesContract.Presenter {


    // Injections
    //
    @Inject
    UserManager userManager;


    // Private Properties
    //
    private IntroThemesContract.View view;
    private ArrayList<String> themes;
    private String currentTheme;

    public IntroThemePresenter(IntroThemesContract.View view) {
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
            case Themes.ORIGINAL_DARK:
                view.themeSelected(R.style.AppTheme_Original_Dark);
                return;
            case Themes.GREEN_DARK:
                view.themeSelected(R.style.AppTheme_Green_Dark);
                return;
            case Themes.PURPLE_DARK:
                view.themeSelected(R.style.AppTheme_Purple_Dark);
                return;
            case Themes.TEAL_DARK:
                view.themeSelected(R.style.AppTheme_Teal_Dark);
                return;
            case Themes.BLUE_GRAY_DARK:
                view.themeSelected(R.style.AppTheme_Blue_Gray_Dark);
                return;
            case Themes.BLACK_DARK:
                view.themeSelected(R.style.AppTheme_Black_Dark);
                return;

            case Themes.ORIGINAL_LIGHT:
                view.themeSelected(R.style.AppTheme_Original_Light);
                return;
            case Themes.GREEN_LIGHT:
                view.themeSelected(R.style.AppTheme_Green_Light);
                return;
            case Themes.PURPLE_LIGHT:
                view.themeSelected(R.style.AppTheme_Purple_Light);
        }
    }


    // Private Functions
    //

    private void setupThemes() {
        themes = Themes.THEMES;
        view.populateThemes(themes, themes.indexOf(userManager.getThemeValue()));
    }
}
