package com.brandonhogan.liftscout.activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import io.realm.Realm;

public class BaseActivity extends AppCompatActivity {


    // Private Properties
    //
    private String classTag = this.getClass().getSimpleName();
    private Realm realm;
    private final Object realmLock = new Object();


    // Public Properties
    //
    public String getClassTag() {
        return classTag;
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
                        Log.e(getClassTag(), "closeRealm() : Failed to close realm");
                    }
                    finally {
                        realm = null;
                    }
                }
            }
        }
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
