package com.brandonhogan.liftscout.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.controls.MaterialProgressBar;
import com.brandonhogan.liftscout.core.model.Category;
import com.brandonhogan.liftscout.core.model.Exercise;
import com.brandonhogan.liftscout.core.model.User;
import com.brandonhogan.liftscout.repository.CategoryRepo;
import com.brandonhogan.liftscout.repository.ExerciseRepo;
import com.brandonhogan.liftscout.repository.impl.CategoryRepoImpl;
import com.brandonhogan.liftscout.repository.impl.ExerciseRepoImpl;

import java.util.ArrayList;

public class SplashActivity extends BaseActivity {

    private ImageView logo;
    private TextView progressBarTitle;
    private MaterialProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = (ImageView)findViewById(R.id.logo_icon);
        progressBarTitle = (TextView) findViewById(R.id.progress_title);
        progressBar = (MaterialProgressBar)findViewById(R.id.progress);

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
                // User has used the app before and should be directed to the main activity
                if (userManager.validUser()){
                    toMain();
                }
                else {
                    initDefaults();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        logo.startAnimation(animation);
    }




    private void initDefaults() {
        progressBarTitle.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        User user = new User(getString(R.string.lbs));
        userManager.setUser(user);

        CategoryRepo categoryRepo = new CategoryRepoImpl();
        ExerciseRepo exerciseRepo = new ExerciseRepoImpl();


        Category abs = categoryRepo.setCategory(createCategory(getString(R.string.category_abs), R.color.category_red));

        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_cable_crunch), abs));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_crunch), abs));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_reverse_crunch), abs));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_russian_twist), abs));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_ab_wheel), abs));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_leg_raise), abs));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_sit_up), abs));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_dumbbell_side_bend), abs));


        Category chest = categoryRepo.setCategory(createCategory(getString(R.string.category_chest), R.color.category_orange));

        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_barbell_flat_bench_press), chest));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_barbell_incline_bench_press), chest));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_barbell_decline_bench_press), chest));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_dumbbell_flat_bench_press), chest));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_dumbbell_incline_bench_press), chest));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_dumbbell_decline_bench_press), chest));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_cable_crossover), chest));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_dumbbell_incline_fly), chest));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_push_up), chest));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_machine_fly), chest));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_dips), chest));


        Category back = categoryRepo.setCategory(createCategory(getString(R.string.category_back), R.color.category_blue));

        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_dumbbell_bent_over_row), back));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_dumbbell_row), back));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_dumbbell_shrug), back));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_barbell_shrug), back));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_barbell_good_morning), back));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_barbell_rack_pull), back));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_barbell_row), back));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_chin_up), back));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_pull_up), back));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_deadlift), back));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_lat_pull_down), back));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_seated_cable_row), back));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_straight_arm_cable_push_down), back));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_yates_row), back));


        Category legs = categoryRepo.setCategory(createCategory(getString(R.string.category_legs), R.color.category_green));

        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_barbell_front_squat), legs));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_barbell_high_squat), legs));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_barbell_low_squat), legs));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_barbell_lunge), legs));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_barbell_calf_raise), legs));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_barbell_glute_bridge), legs));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_hamstring_curl), legs));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_leg_extension), legs));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_romanian_deadlift), legs));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_leg_press), legs));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_dumbbell_lunge), legs));


        Category shoulders = categoryRepo.setCategory(createCategory(getString(R.string.category_shoulders), R.color.category_yellow));

        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_dumbbell_arnold_press), shoulders));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_dumbbell_face_pull), shoulders));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_dumbbell_lateral_raise), shoulders));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_dumbbell_overhead_press), shoulders));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_dumbbell_seated_overhead_press), shoulders));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_dumbbell_rear_delt_raise), shoulders));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_dumbbell_front_raise), shoulders));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_cable_face_pull), shoulders));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_bent_over_low_cable_delt_raise), shoulders));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_cable_lateral_raise), shoulders));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_cable_rear_delt_fly), shoulders));


        Category biceps = categoryRepo.setCategory(createCategory(getString(R.string.category_biceps), R.color.category_brown));

        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_barbell_curl), biceps));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_barbell_drag_curl), biceps));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_ez_bar_curl), biceps));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_ez_bar_preacher_curl), biceps));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_dumbbell_curl), biceps));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_dumbbell_hammer_curl), biceps));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_dumbbell_preacher_curl), biceps));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_dumbbell_seated_incline_curl), biceps));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_cable_curl), biceps));


        Category triceps = categoryRepo.setCategory(createCategory(getString(R.string.category_triceps), R.color.category_dark_pink));

        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_rope_push_down), triceps));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_ez_bar_skullcrusher), triceps));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_barbell_skullcrusher), triceps));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_barbell_close_grip_bench_press), triceps));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_dumbbell_overhead_tricep_extension), triceps));
        exerciseRepo.setExercise(createExercise(getString(R.string.exercise_ring_dip), triceps));


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toMain();
            }
        }, 4000);
    }

    private Category createCategory(String name, int color) {

        Category category = new Category();
        category.setName(name);
        category.setColor(getResources().getColor(color));

        return category;
    }

    private Exercise createExercise(String name, Category category) {
        Exercise exercise = new Exercise();
        exercise.setName(name);
        exercise.setCategoryId(category.getId());
        return exercise;
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
