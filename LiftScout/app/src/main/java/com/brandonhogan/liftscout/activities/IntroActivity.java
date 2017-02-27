package com.brandonhogan.liftscout.activities;

import android.content.Intent;
import android.support.annotation.FloatRange;
import android.os.Bundle;
import android.view.View;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.views.Intro.IntroSettingsSlideFragment;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;

public class IntroActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        enableLastSlideAlphaExitTransition(true);


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

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.intro_slide_two)
                .buttonsColor(R.color.colorAccent_ThemeGreen)
                .image(R.drawable.icon_grey_xxhdpi)
                .title(getString(R.string.intro_slide_one_title))
                .description(getString(R.string.intro_slide_one_description))
                .build());


        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.intro_slide_three)
                .buttonsColor(R.color.colorAccent_ThemeGreen)
                .image(R.drawable.icon_grey_xxhdpi)
                .title(getString(R.string.intro_slide_one_title))
                .description(getString(R.string.intro_slide_one_description))
                .build());





    }



    private void toMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        finish();
    }
}
