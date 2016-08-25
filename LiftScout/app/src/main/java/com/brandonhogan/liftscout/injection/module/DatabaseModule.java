package com.brandonhogan.liftscout.injection.module;

import com.brandonhogan.liftscout.repository.DatabaseRealm;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    DatabaseRealm databaseRealm() {
        return new DatabaseRealm();
    }
}
