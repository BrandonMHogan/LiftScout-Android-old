package com.brandonhogan.liftscout.injection.components;

import com.brandonhogan.liftscout.activities.BaseActivity;
import com.brandonhogan.liftscout.activities.InitActivity;
import com.brandonhogan.liftscout.activities.MainActivity;
import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.injection.module.AppModule;
import com.brandonhogan.liftscout.injection.module.DatabaseModule;
import com.brandonhogan.liftscout.injection.module.UserModule;
import com.brandonhogan.liftscout.repository.impl.CategoryRepoImpl;
import com.brandonhogan.liftscout.repository.impl.ExerciseRepoImpl;
import com.brandonhogan.liftscout.repository.impl.ProgressRepoImpl;
import com.brandonhogan.liftscout.repository.impl.SetRepoImpl;
import com.brandonhogan.liftscout.repository.impl.UserRepoImpl;
import com.brandonhogan.liftscout.repository.impl.UserSettingsRepoImpl;
import com.brandonhogan.liftscout.views.categories.CategoryListPresenter;
import com.brandonhogan.liftscout.views.exercises.ExerciseListPresenter;
import com.brandonhogan.liftscout.views.home.HomeContainerFragment;
import com.brandonhogan.liftscout.views.home.TodayFragment;
import com.brandonhogan.liftscout.views.settings.display.SettingsDisplayFragment;
import com.brandonhogan.liftscout.views.settings.display.SettingsDisplayPresenter;
import com.brandonhogan.liftscout.views.settings.home.SettingsHomePresenter;
import com.brandonhogan.liftscout.views.workout.tracker.TrackerFragment;
import com.brandonhogan.liftscout.views.workout.tracker.TrackerPresenter;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, DatabaseModule.class, UserModule.class})
public interface AppComponent {

    // Managers
    void inject(ProgressManager manager);

    // Activities
    void inject(BaseActivity activity);
    void inject(InitActivity activity);
    void inject(MainActivity activity);

    // Fragments
    void inject(HomeContainerFragment fragment);
    void inject(TodayFragment fragment);

    // Category / Exercise
    void inject(ExerciseListPresenter presenter);
    void inject(CategoryListPresenter presenter);

    // Workout
    void inject(TrackerFragment fragment);
    void inject(TrackerPresenter presenter);

    // Settings
    void inject(SettingsDisplayFragment fragment);
    void inject(SettingsDisplayPresenter presenter);
    void inject(SettingsHomePresenter presenter);


    // Repos
    void inject(UserRepoImpl repo);
    void inject(UserSettingsRepoImpl repoImpl);
    void inject(ProgressRepoImpl repoImpl);
    void inject(SetRepoImpl repoImpl);
    void inject(CategoryRepoImpl repoImpl);
    void inject(ExerciseRepoImpl repoImpl);
}
