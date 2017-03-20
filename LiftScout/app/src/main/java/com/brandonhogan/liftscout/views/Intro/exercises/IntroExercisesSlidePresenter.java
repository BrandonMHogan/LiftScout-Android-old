package com.brandonhogan.liftscout.views.Intro.exercises;

import android.util.Log;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.managers.UserManager;
import com.brandonhogan.liftscout.core.model.Category;
import com.brandonhogan.liftscout.core.model.Exercise;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.CategoryRepo;
import com.brandonhogan.liftscout.repository.ExerciseRepo;
import com.brandonhogan.liftscout.repository.impl.CategoryRepoImpl;
import com.brandonhogan.liftscout.repository.impl.ExerciseRepoImpl;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Brandon on 2/27/2017.
 * Description :
 */

public class IntroExercisesSlidePresenter implements IntroExercisesSlideContract.Presenter {

    private static String TAG = "IExercisesSlideP";


    // Injections
    //
    @Inject
    UserManager userManager;


    // Private Properties
    //
    private IntroExercisesSlideContract.View view;

    public IntroExercisesSlidePresenter(IntroExercisesSlideContract.View view) {
        Injector.getAppComponent().inject(this);
        this.view = view;
    }


    // Contract
    //
    @Override
    public void viewCreated() {
    }

    @Override
    public void onButtonPressed() {
        setupDefaults().subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                try {
                    view.exercisesCreated("Exercises Created!");
                }
                catch (Exception ex) {
                    Log.e(TAG, "accept: ", ex);
                }
            }
        });
    }

    private Observable<Boolean> setupDefaults() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {

                try {
                    CategoryRepo categoryRepo = new CategoryRepoImpl();
                    ExerciseRepo exerciseRepo = new ExerciseRepoImpl();

                    Category abs = categoryRepo.setCategory(createCategory(view.getStringValue(R.string.category_abs), R.color.category_light_red));

                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_cable_crunch), abs));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_crunch), abs));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_reverse_crunch), abs));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_russian_twist), abs));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_ab_wheel), abs));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_leg_raise), abs));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_sit_up), abs));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_dumbbell_side_bend), abs));


                    Category chest = categoryRepo.setCategory(createCategory(view.getStringValue(R.string.category_chest), R.color.category_orange));

                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_barbell_flat_bench_press), chest));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_barbell_incline_bench_press), chest));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_barbell_decline_bench_press), chest));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_dumbbell_flat_bench_press), chest));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_dumbbell_incline_bench_press), chest));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_dumbbell_decline_bench_press), chest));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_cable_crossover), chest));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_dumbbell_incline_fly), chest));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_push_up), chest));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_machine_fly), chest));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_dips), chest));


                    Category back = categoryRepo.setCategory(createCategory(view.getStringValue(R.string.category_back), R.color.category_blue));

                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_dumbbell_bent_over_row), back));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_dumbbell_row), back));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_dumbbell_shrug), back));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_barbell_shrug), back));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_barbell_good_morning), back));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_barbell_rack_pull), back));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_barbell_row), back));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_chin_up), back));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_pull_up), back));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_deadlift), back));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_lat_pull_down), back));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_seated_cable_row), back));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_straight_arm_cable_push_down), back));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_yates_row), back));


                    Category legs = categoryRepo.setCategory(createCategory(view.getStringValue(R.string.category_legs), R.color.category_green));

                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_barbell_front_squat), legs));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_barbell_high_squat), legs));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_barbell_low_squat), legs));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_barbell_lunge), legs));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_barbell_calf_raise), legs));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_barbell_glute_bridge), legs));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_hamstring_curl), legs));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_leg_extension), legs));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_romanian_deadlift), legs));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_leg_press), legs));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_dumbbell_lunge), legs));


                    Category shoulders = categoryRepo.setCategory(createCategory(view.getStringValue(R.string.category_shoulders), R.color.category_yellow));

                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_dumbbell_arnold_press), shoulders));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_dumbbell_face_pull), shoulders));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_dumbbell_lateral_raise), shoulders));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_dumbbell_overhead_press), shoulders));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_dumbbell_seated_overhead_press), shoulders));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_dumbbell_rear_delt_raise), shoulders));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_dumbbell_front_raise), shoulders));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_cable_face_pull), shoulders));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_bent_over_low_cable_delt_raise), shoulders));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_cable_lateral_raise), shoulders));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_cable_rear_delt_fly), shoulders));


                    Category biceps = categoryRepo.setCategory(createCategory(view.getStringValue(R.string.category_biceps), R.color.category_light_green));

                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_barbell_curl), biceps));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_barbell_drag_curl), biceps));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_ez_bar_curl), biceps));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_ez_bar_preacher_curl), biceps));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_dumbbell_curl), biceps));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_dumbbell_hammer_curl), biceps));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_dumbbell_preacher_curl), biceps));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_dumbbell_seated_incline_curl), biceps));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_cable_curl), biceps));


                    Category triceps = categoryRepo.setCategory(createCategory(view.getStringValue(R.string.category_triceps), R.color.category_pink));

                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_rope_push_down), triceps));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_ez_bar_skullcrusher), triceps));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_barbell_skullcrusher), triceps));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_barbell_close_grip_bench_press), triceps));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_dumbbell_overhead_tricep_extension), triceps));
                    exerciseRepo.setExercise(createExercise(view.getStringValue(R.string.exercise_ring_dip), triceps));

                    userManager.setLoadedDefaultExercises(true);

                    e.onNext(true);
                    e.onComplete();
                }
                catch (Exception ex) {
                    Log.e(TAG, "subscribe: ", ex);
                    e.onError(ex);
                }
            }
        });
    }

    private Category createCategory(String name, int color) {

        Category category = new Category();
        category.setName(name);
        category.setColor(view.getColor(color));

        return category;
    }

    private Exercise createExercise(String name, Category category) {
        Exercise exercise = new Exercise();
        exercise.setName(name);
        exercise.setCategoryId(category.getId());
        return exercise;
    }
}
