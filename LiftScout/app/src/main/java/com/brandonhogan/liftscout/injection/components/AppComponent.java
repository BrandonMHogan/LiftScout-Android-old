package com.brandonhogan.liftscout.injection.components;

import com.brandonhogan.liftscout.activities.MainActivity;
import com.brandonhogan.liftscout.injection.module.AppModule;
import com.brandonhogan.liftscout.injection.module.DatabaseModule;
import com.brandonhogan.liftscout.injection.module.UserModule;
import com.brandonhogan.liftscout.repository.impl.UserRepoImpl;
import com.brandonhogan.liftscout.repository.impl.UserSettingsRepoImpl;
import com.brandonhogan.liftscout.views.home.HomeContainerFragment;
import com.brandonhogan.liftscout.views.settings.display.SettingsDisplayFragment;
import com.brandonhogan.liftscout.views.settings.display.SettingsDisplayPresenter;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, DatabaseModule.class, UserModule.class})
public interface AppComponent {

    // Activities
    void inject(MainActivity activity);

    // Fragments
    void inject(HomeContainerFragment fragment);

    // Settings
    void inject(SettingsDisplayFragment fragment);
    void inject(SettingsDisplayPresenter presenter);


    // Repos
    void inject(UserRepoImpl userRepo);
    void inject(UserSettingsRepoImpl userSettingsRepo);
}
