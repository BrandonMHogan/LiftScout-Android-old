package com.brandonhogan.liftscout.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.model.User;

public class SplashActivity extends BaseActivity {

    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = (ImageView)findViewById(R.id.logo_icon);

        startUpAnimation();
    }

    private void startUpAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_in);
        animation.setInterpolator((new AccelerateDecelerateInterpolator()));

        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                User user = getRealm().where(User.class).findFirst();

                // User has used the app before and should be directed to the main activity
                if (user != null){
                    toMain();
                }
                else {
                    toInit();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        logo.startAnimation(animation);
    }




    private void toInit() {
        Intent intent = new Intent(this, InitActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION |
                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivityForResult(intent, 0);
        finish();
    }

    private void toMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}
