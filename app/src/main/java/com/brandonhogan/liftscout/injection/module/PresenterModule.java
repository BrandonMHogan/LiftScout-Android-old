package com.brandonhogan.liftscout.injection.module;

import com.brandonhogan.liftscout.interfaces.contracts.SettingsProfileContract;
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

    @Provides
    @Singleton
    SettingsProfileContract.Presenter getSettingsProfilePresenter() {
        return new SettingsProfilePresenter();
    }

}
