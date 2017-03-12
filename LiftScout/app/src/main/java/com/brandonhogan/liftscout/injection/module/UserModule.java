package com.brandonhogan.liftscout.injection.module;

import com.brandonhogan.liftscout.core.managers.CalendarManager;
import com.brandonhogan.liftscout.core.managers.GraphManager;
import com.brandonhogan.liftscout.core.managers.NavigationManager;
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
    NavigationManager navigationManager() {
        return new NavigationManager();
    }

    @Provides
    @Singleton
    ProgressManager progressManager() {
        return new ProgressManager();
    }

    @Provides
    @Singleton
    CalendarManager calendarManager() {
        return new CalendarManager();
    }

    @Provides
    @Singleton
    GraphManager graphManager() {
        return new GraphManager();
    }
}
