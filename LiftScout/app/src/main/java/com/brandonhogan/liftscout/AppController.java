package com.brandonhogan.liftscout;

import android.app.Application;

import com.brandonhogan.liftscout.injection.components.Injector;
import com.squareup.leakcanary.LeakCanary;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AppController extends Application {

    protected static AppController mInstance;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        initDagger();
        initRealmConfiguration();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    private void initDagger() {
        Injector.initAppComponent(this);
    }

    private void initRealmConfiguration() {
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
