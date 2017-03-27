package com.brandonhogan.liftscout.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.view.View;
import android.widget.Toast;

import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.views.IntroExerciseFragment;
import com.brandonhogan.liftscout.views.IntroFirstFragment;
import com.brandonhogan.liftscout.views.IntroLastFragment;
import com.brandonhogan.liftscout.views.IntroSettingsFragment;
import com.brandonhogan.liftscout.views.IntroThemeFragment;

import javax.inject.Inject;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.animations.IViewTranslation;

public class IntroActivity extends MaterialIntroActivity {

    @Inject
    UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.getAppComponent().inject(this);


        enableLastSlideAlphaExitTransition(false);


        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });

        addSlide(new IntroFirstFragment());
        addSlide(new IntroSettingsFragment());
        addSlide(new IntroExerciseFragment());
        addSlide(new IntroThemeFragment());
        addSlide(new IntroLastFragment());
    }

    @Override
    public void onFinish() {
        super.onFinish();
        toMain();
    }

    private void makeToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    private void toMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        finish();
    }
}