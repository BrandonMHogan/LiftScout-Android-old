package com.brandonhogan.liftscout.injection.module;

import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.core.managers.UserManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {

    @Provides
    @Singleton
    UserManager userManager() {
        return new UserManager();
    }

    @Provides
    @Singleton
    ProgressManager progressManager() {
        return new ProgressManager();
    }
}
