package com.brandonhogan.liftscout.injection.components;

import com.brandonhogan.liftscout.activities.MainActivity;
import com.brandonhogan.liftscout.injection.module.AppModule;
import com.brandonhogan.liftscout.injection.module.DatabaseModule;
import com.brandonhogan.liftscout.injection.module.UserModule;
import com.brandonhogan.liftscout.repository.impl.UserRepoImpl;
import com.brandonhogan.liftscout.views.home.HomeContainerFragment;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, DatabaseModule.class, UserModule.class})
public interface AppComponent {

    void inject(HomeContainerFragment fragment);
    void inject(MainActivity activity);



    // Repos
    void inject(UserRepoImpl userRepoImpl);
}
