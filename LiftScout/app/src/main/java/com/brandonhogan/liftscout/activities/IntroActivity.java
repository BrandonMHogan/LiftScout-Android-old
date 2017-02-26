package com.brandonhogan.liftscout.activities;

import android.content.Intent;
import android.support.annotation.FloatRange;
import android.os.Bundle;
import android.view.View;

import com.brandonhogan.liftscout.R;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
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
                        .backgroundColor(R.color.blue)
                        .buttonsColor(R.color.gray_dark)
                        .image(R.drawable.ic_add_white_24dp)
                        .title("Organize your time with us")
                        .description("Would you try?")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage("We provide solutions to make you love your work");
                    }
                }, "Work with love"));


        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.green)
                .buttonsColor(R.color.red)
                .title("Want more?")
                .description("Go on")
                .build());



        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.blue)
                        .buttonsColor(R.color.gray_dark)
                        .image(R.drawable.ic_add_white_24dp)
                        .title("Organize your time with us")
                        .description("Would you try?")
                        .build());
    }



    private void toMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        finish();
    }
}
