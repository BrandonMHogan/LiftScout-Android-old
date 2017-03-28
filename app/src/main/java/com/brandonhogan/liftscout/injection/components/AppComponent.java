package com.brandonhogan.liftscout.injection.components;

import com.brandonhogan.liftscout.activities.BaseActivity;
import com.brandonhogan.liftscout.activities.IntroActivity;
import com.brandonhogan.liftscout.activities.MainActivity;
import com.brandonhogan.liftscout.injection.module.AppModule;
import com.brandonhogan.liftscout.injection.module.DatabaseModule;
import com.brandonhogan.liftscout.injection.module.UserModule;
import com.brandonhogan.liftscout.managers.ProgressManager;
import com.brandonhogan.liftscout.presenters.CalendarPresenter;
import com.brandonhogan.liftscout.presenters.CategoryListPresenter;
import com.brandonhogan.liftscout.presenters.ExerciseDetailPresenter;
import com.brandonhogan.liftscout.presenters.ExerciseListPresenter;
import com.brandonhogan.liftscout.presenters.GraphExercisesPresenter;
import com.brandonhogan.liftscout.presenters.GraphsCategoriesPresenter;
import com.brandonhogan.liftscout.presenters.IntroExercisePresenter;
import com.brandonhogan.liftscout.presenters.IntroSettingsPresenter;
import com.brandonhogan.liftscout.presenters.IntroThemePresenter;
import com.brandonhogan.liftscout.presenters.RecordsPresenter;
import com.brandonhogan.liftscout.presenters.SettingsDisplayPresenter;
import com.brandonhogan.liftscout.presenters.SettingsHomePresenter;
import com.brandonhogan.liftscout.presenters.SettingsProfilePresenter;
import com.brandonhogan.liftscout.presenters.TodayContainerPresenter;
import com.brandonhogan.liftscout.presenters.TodayPresenter;
import com.brandonhogan.liftscout.presenters.WorkoutContainerPresenter;
import com.brandonhogan.liftscout.presenters.WorkoutGraphPresenter;
import com.brandonhogan.liftscout.presenters.WorkoutHistoryPresenter;
import com.brandonhogan.liftscout.presenters.WorkoutTrackerPresenter;
import com.brandonhogan.liftscout.repository.impl.CategoryRepoImpl;
import com.brandonhogan.liftscout.repository.impl.ExerciseRepoImpl;
import com.brandonhogan.liftscout.repository.impl.ProgressRepoImpl;
import com.brandonhogan.liftscout.repository.impl.RecordsRepoImpl;
import com.brandonhogan.liftscout.repository.impl.SetRepoImpl;
import com.brandonhogan.liftscout.repository.impl.UserRepoImpl;
import com.brandonhogan.liftscout.repository.impl.UserSettingsRepoImpl;
import com.brandonhogan.liftscout.utils.controls.graphs.line.MyLineGraph;
import com.brandonhogan.liftscout.views.GraphExercisesFragment;
import com.brandonhogan.liftscout.views.SettingsDisplayFragment;
import com.brandonhogan.liftscout.views.TrackerFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, DatabaseModule.class, UserModule.class})
public interface AppComponent {

    // Managers
    void inject(ProgressManager manager);

    // Activities
    void inject(BaseActivity activity);
    void inject(IntroActivity activity);
    void inject(MainActivity activity);

    // Fragments
    void inject(TodayContainerPresenter presenter);
    void inject(TodayPresenter presenter);

    // Intro
    void inject(IntroSettingsPresenter presenter);
    void inject(IntroExercisePresenter presenter);
    void inject(IntroThemePresenter presenter);

    // Calendar
    void inject(CalendarPresenter presenter);

    // Graphs
    void inject(GraphsCategoriesPresenter presenter);
    void inject(GraphExercisesPresenter presenter);
    void inject(GraphExercisesFragment presenter);
    void inject(MyLineGraph view);

    // Category / Exercise
    void inject(ExerciseListPresenter presenter);
    void inject(CategoryListPresenter presenter);
    void inject(ExerciseDetailPresenter presenter);

    // Workout
    void inject(WorkoutContainerPresenter presenter);
    void inject(TrackerFragment fragment);
    void inject(WorkoutTrackerPresenter presenter);
    void inject(WorkoutHistoryPresenter presenter);
    void inject(WorkoutGraphPresenter presenter);
    void inject(RecordsPresenter presenter);

    // Settings
    void inject(SettingsDisplayFragment fragment);
    void inject(SettingsDisplayPresenter presenter);
    void inject(SettingsHomePresenter presenter);
    void inject(SettingsProfilePresenter presenter);


    // Repos
    void inject(UserRepoImpl repo);
    void inject(UserSettingsRepoImpl repoImpl);
    void inject(ProgressRepoImpl repoImpl);
    void inject(SetRepoImpl repoImpl);
    void inject(CategoryRepoImpl repoImpl);
    void inject(ExerciseRepoImpl repoImpl);
    void inject(RecordsRepoImpl repoImpl);

}
