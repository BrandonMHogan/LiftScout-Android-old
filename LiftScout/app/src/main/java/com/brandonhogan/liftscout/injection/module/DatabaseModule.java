package com.brandonhogan.liftscout.injection.module;

import com.brandonhogan.liftscout.repository.DatabaseRealm;
import com.brandonhogan.liftscout.repository.RecordsRepo;
import com.brandonhogan.liftscout.repository.SetRepo;
import com.brandonhogan.liftscout.repository.impl.RecordsRepoImpl;
import com.brandonhogan.liftscout.repository.impl.SetRepoImpl;

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

    @Provides
    @Singleton
    RecordsRepo recordRepo() {
        return new RecordsRepoImpl();
    }

    @Provides
    @Singleton
    SetRepo setRepo() {
        return new SetRepoImpl();
    }

}
