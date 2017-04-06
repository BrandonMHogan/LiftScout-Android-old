package com.brandonhogan.liftscout.injection.module;

import com.brandonhogan.liftscout.interfaces.contracts.IntroSettingsContract;
import com.brandonhogan.liftscout.interfaces.contracts.IntroThemesContract;
import com.brandonhogan.liftscout.interfaces.contracts.SettingsDisplayContract;
import com.brandonhogan.liftscout.interfaces.contracts.SettingsHomeContract;
import com.brandonhogan.liftscout.interfaces.contracts.SettingsProfileContract;
import com.brandonhogan.liftscout.presenters.IntroSettingsPresenter;
import com.brandonhogan.liftscout.presenters.IntroThemesPresenter;
import com.brandonhogan.liftscout.presenters.SettingsDisplayPresenter;
import com.brandonhogan.liftscout.presenters.SettingsHomePresenter;
import com.brandonhogan.liftscout.presenters.SettingsProfilePresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Brandon on 4/5/2017.
 * Description :
 */
@Module
public class PresenterModule {

    // Settings
    //
    @Provides
    @Singleton
    SettingsProfileContract.Presenter getSettingsProfilePresenter() {
        return new SettingsProfilePresenter();
    }
    @Provides
    @Singleton
    SettingsDisplayContract.Presenter getSettingsDisplayPresenter() {
        return new SettingsDisplayPresenter();
    }
    @Provides
    @Singleton
    SettingsHomeContract.Presenter getSettingsHomeContract() {
        return new SettingsHomePresenter();
    }


    // Intro
    //
    @Provides
    @Singleton
    IntroSettingsContract.Presenter getIntroSettingsPresenter() {
        return new IntroSettingsPresenter();
    }

    @Provides
    @Singleton
    IntroThemesContract.Presenter getIntroThemePresenter() {
        return new IntroThemesPresenter();
    }

}
