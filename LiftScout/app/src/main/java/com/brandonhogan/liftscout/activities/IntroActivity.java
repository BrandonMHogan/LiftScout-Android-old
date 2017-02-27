package com.brandonhogan.liftscout.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.FloatRange;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.managers.UserManager;
import com.brandonhogan.liftscout.core.model.Category;
import com.brandonhogan.liftscout.core.model.Exercise;
import com.brandonhogan.liftscout.core.model.User;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.CategoryRepo;
import com.brandonhogan.liftscout.repository.ExerciseRepo;
import com.brandonhogan.liftscout.repository.impl.CategoryRepoImpl;
import com.brandonhogan.liftscout.repository.impl.ExerciseRepoImpl;
import com.brandonhogan.liftscout.views.Intro.exercises.IntroExercisesSlideFragment;
import com.brandonhogan.liftscout.views.Intro.settings.IntroSettingsSlideFragment;

import javax.inject.Inject;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
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


        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.intro_slide_one)
                        .buttonsColor(R.color.colorAccent_ThemeGreen)
                        .image(R.drawable.icon_grey_xxhdpi)
                        .title(getString(R.string.intro_slide_one_title))
                        .description(getString(R.string.intro_slide_one_description))
                        .build());


        addSlide(new IntroSettingsSlideFragment());
        addSlide(new IntroExercisesSlideFragment());


        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.intro_slide_three)
                .buttonsColor(R.color.colorAccent_ThemeGreen)
                .image(R.drawable.ic_invert_colors_white_48dp)
                .title(getString(R.string.intro_slide_one_title))
                .description(getString(R.string.intro_slide_one_description))
                .build());


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
