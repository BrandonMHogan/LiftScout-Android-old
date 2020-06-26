package com.brandonhogan.liftscout.activities;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.utils.constants.Themes;
import com.google.firebase.analytics.FirebaseAnalytics;

import javax.inject.Inject;

public class BaseActivity extends AppCompatActivity {

    @Inject
    UserManager userManager;


    // Private Properties
    //
    private String TAG = this.getClass().getSimpleName();
    private FirebaseAnalytics mFirebaseAnalytics;


    // Public Properties
    //
    public String getTAG() {
        return TAG;
    }

    public FirebaseAnalytics getFirebaseAnalytics() {
        return mFirebaseAnalytics;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Injector.getAppComponent().inject(this);

        if (userManager.getThemeValue().equals(Themes.ORIGINAL_LIGHT))
            setTheme(R.style.AppTheme_Original_Light);
        else if(userManager.getThemeValue().equals(Themes.GREEN_LIGHT))
            setTheme(R.style.AppTheme_Green_Light);
        else if(userManager.getThemeValue().equals(Themes.PURPLE_LIGHT))
            setTheme(R.style.AppTheme_Purple_Light);


        else if(userManager.getThemeValue().equals(Themes.GREEN_DARK))
            setTheme(R.style.AppTheme_Green_Dark);
        else if(userManager.getThemeValue().equals(Themes.PURPLE_DARK))
            setTheme(R.style.AppTheme_Purple_Dark);
        else if(userManager.getThemeValue().equals(Themes.TEAL_DARK))
            setTheme(R.style.AppTheme_Teal_Dark);
        else if(userManager.getThemeValue().equals(Themes.BLUE_GRAY_DARK))
            setTheme(R.style.AppTheme_Blue_Gray_Dark);
        else if(userManager.getThemeValue().equals(Themes.BLUE_GRAY_DARK))
            setTheme(R.style.AppTheme_Blue_Gray_Dark);
        else if(userManager.getThemeValue().equals(Themes.BLACK_DARK))
            setTheme(R.style.AppTheme_Black_Dark);
        else
            setTheme(R.style.AppTheme_Original_Dark);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        super.onCreate(savedInstanceState);
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    // Overrides

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
