package com.brandonhogan.liftscout.injection.components;

import android.app.Application;

import com.brandonhogan.liftscout.injection.module.AppModule;
import com.brandonhogan.liftscout.injection.module.DatabaseModule;
import com.brandonhogan.liftscout.injection.module.UserModule;

public class Injector {

    private static AppComponent appComponent;
    private static RepoComponent repoComponent;

    private Injector() {}


    // This should only ever be called by the AppController.
    public static void initComponents(Application application) {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(application))
                .userModule(new UserModule())
                .databaseModule(new DatabaseModule())
                .build();

        repoComponent = DaggerRepoComponent.builder()
                .databaseModule(new DatabaseModule())
                .build();
    }

    public static AppComponent getAppComponent() {
       // Objects.requireNonNull(appComponent, "appComponent is null");
        return appComponent;
    }

    public static RepoComponent getRepoComponent() {
       // Objects.requireNonNull(repoComponent, "repoComponent is null");
        return repoComponent;
    }
}
