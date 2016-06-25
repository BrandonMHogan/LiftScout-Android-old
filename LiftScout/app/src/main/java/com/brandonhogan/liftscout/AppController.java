package com.brandonhogan.liftscout;

import android.app.Application;

import java.util.logging.Logger;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AppController extends Application {

    private static AppController mInstance;
  //  private Tracker mTracker;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        //Establishes the default realm and its configuration
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }


//
//    /**
//     * Gets the default {@link Tracker} for this {@link Application}.
//     * @return tracker
//     */
//    synchronized public Tracker getDefaultTracker() {
//        if (mTracker == null) {
//            GoogleAnalytics analytics = GoogleAnalytics.getInstance(mInstance);
//            analytics.setLocalDispatchPeriod(1);
//            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
//            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
//            mTracker = analytics.newTracker(R.xml.wc_tracker);
//            mTracker.setAppVersion(DeviceInfo.getApplicationVersion());
//            mTracker.setAppId(DeviceInfo.getId());
//            mTracker.enableExceptionReporting(true);
//
//        }
//        return mTracker;
//    }
}
