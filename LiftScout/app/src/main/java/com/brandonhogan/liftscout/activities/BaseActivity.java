package com.brandonhogan.liftscout.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.foundation.constants.Themes;
import com.brandonhogan.liftscout.foundation.model.UserSetting;
import com.google.firebase.analytics.FirebaseAnalytics;

import io.realm.Realm;
import io.realm.RealmResults;

public class BaseActivity extends AppCompatActivity {


    // Private Properties
    //
    private String TAG = this.getClass().getSimpleName();
    private Realm realm;
    private final Object realmLock = new Object();
    private FirebaseAnalytics mFirebaseAnalytics;


    // Public Properties
    //
    public String getTAG() {
        return TAG;
    }

    public Realm getRealm() {
        if (realm == null || realm.isClosed()) {
            synchronized (realmLock) {
                if (realm == null || realm.isClosed()) {
                    realm = Realm.getDefaultInstance();
                }
            }
        }
        return realm;
    }

    public FirebaseAnalytics getFirebaseAnalytics() {
        return mFirebaseAnalytics;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (getDisplayTheme().getValue().equals(Themes.LIGHT))
            setTheme(R.style.AppTheme_Light);
        else
            setTheme(R.style.AppTheme_Dark);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        super.onCreate(savedInstanceState);
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    // Private Functions
    //
    private void closeRealm() {
        if (realm != null && !realm.isClosed()) {
            synchronized (realmLock) {
                if (realm != null && !realm.isClosed()) {
                    try {
                        realm.close();
                    }
                    catch (Exception ex) {
                        Log.e(getTAG(), "closeRealm() : Failed to close realm");
                    }
                    finally {
                        realm = null;
                    }
                }
            }
        }
    }

    private UserSetting getDisplayTheme() {
        UserSetting setting = getRealm().where(UserSetting.class)
                .equalTo(UserSetting.NAME, UserSetting.THEME).findFirst();

        if (setting == null || !setting.isValid()) {

            setting = new UserSetting();
            setting.setName(UserSetting.THEME);
            setting.setValue(Themes.DARK);

            getRealm().beginTransaction();
            getRealm().copyToRealmOrUpdate(setting);
            getRealm().commitTransaction();
        }

        return setting;
    }

    // Overrides

    @Override
    public void onStop() {
        super.onStop();
        closeRealm();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeRealm();
    }
}
