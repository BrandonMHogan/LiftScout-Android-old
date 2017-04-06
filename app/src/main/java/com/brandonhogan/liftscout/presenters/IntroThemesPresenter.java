package com.brandonhogan.liftscout.presenters;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.IntroThemesContract;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.utils.constants.Themes;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class IntroThemesPresenter implements IntroThemesContract.Presenter {

    // Injections
    //
    @Inject
    UserManager userManager;

    // Private Properties
    //
    private IntroThemesContract.View view;

    @Inject
    public IntroThemesPresenter() {
        Injector.getAppComponent().inject(this);
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void setView(IntroThemesContract.View view) {
        this.view = view;
        init();
    }

    @Override
    public void onThemeSelected(int position) {
        String currentTheme = Themes.THEMES.get(position);
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

    private void init() {
        setupThemes();
    }

    private void setupThemes() {
        view.populateThemes(Themes.THEMES, Themes.THEMES.indexOf(userManager.getThemeValue()));
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}
