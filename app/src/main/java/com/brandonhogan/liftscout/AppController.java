package com.brandonhogan.liftscout;

import android.app.Application;

import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.migration.Migration;

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

    }

    private void initDagger() {
        Injector.initComponents(this);
    }

    private void initRealmConfiguration() {

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("myRealm.realm")
                .schemaVersion(1)
                .migration(new Migration())
                //.deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
