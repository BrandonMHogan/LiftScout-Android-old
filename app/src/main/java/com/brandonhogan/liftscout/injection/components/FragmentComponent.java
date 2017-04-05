package com.brandonhogan.liftscout.injection.components;

import com.brandonhogan.liftscout.injection.module.PresenterModule;
import com.brandonhogan.liftscout.injection.module.UserModule;
import com.brandonhogan.liftscout.views.SettingsDisplayFragment;
import com.brandonhogan.liftscout.views.SettingsProfileFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Brandon on 4/5/2017.
 * Description :
 */

@Singleton
@Component(modules = {UserModule.class, PresenterModule.class})
public interface FragmentComponent {

    // Settings
    void inject(SettingsProfileFragment fragment);
    void inject(SettingsDisplayFragment fragment);
}
