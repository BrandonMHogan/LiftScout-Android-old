package com.brandonhogan.liftscout.injection.components;

import com.brandonhogan.liftscout.activities.BaseActivity;
import com.brandonhogan.liftscout.activities.IntroActivity;
import com.brandonhogan.liftscout.activities.MainActivity;
import com.brandonhogan.liftscout.core.controls.graphs.line.MyLineGraph;
import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.injection.module.AppModule;
import com.brandonhogan.liftscout.injection.module.DatabaseModule;
import com.brandonhogan.liftscout.injection.module.UserModule;
import com.brandonhogan.liftscout.repository.impl.CategoryRepoImpl;
import com.brandonhogan.liftscout.repository.impl.ExerciseRepoImpl;
import com.brandonhogan.liftscout.repository.impl.ProgressRepoImpl;
import com.brandonhogan.liftscout.repository.impl.RecordRepoImpl;
import com.brandonhogan.liftscout.repository.impl.SetRepoImpl;
import com.brandonhogan.liftscout.repository.impl.UserRepoImpl;
import com.brandonhogan.liftscout.repository.impl.UserSettingsRepoImpl;
import com.brandonhogan.liftscout.views.Intro.exercises.IntroExercisesSlidePresenter;
import com.brandonhogan.liftscout.views.Intro.settings.IntroSettingsSlidePresenter;
import com.brandonhogan.liftscout.views.Intro.themes.IntroThemeSlidePresenter;
import com.brandonhogan.liftscout.views.calendar.CalendarPresenter;
import com.brandonhogan.liftscout.views.categories.CategoryListPresenter;
import com.brandonhogan.liftscout.views.exercises.ExerciseListPresenter;
import com.brandonhogan.liftscout.views.graphs.categories.GraphsCategoriesPresenter;
import com.brandonhogan.liftscout.views.graphs.exercises.GraphExercisesFragment;
import com.brandonhogan.liftscout.views.graphs.exercises.GraphExercisesPresenter;
import com.brandonhogan.liftscout.views.home.HomePresenter;
import com.brandonhogan.liftscout.views.home.today.TodayPresenter;
import com.brandonhogan.liftscout.views.settings.display.SettingsDisplayFragment;
import com.brandonhogan.liftscout.views.settings.display.SettingsDisplayPresenter;
import com.brandonhogan.liftscout.views.settings.home.SettingsHomePresenter;
import com.brandonhogan.liftscout.views.settings.profile.SettingsProfilePresenter;
import com.brandonhogan.liftscout.views.workout.WorkoutContainerPresenter;
import com.brandonhogan.liftscout.views.workout.graph.GraphPresenter;
import com.brandonhogan.liftscout.views.workout.history.HistoryPresenter;
import com.brandonhogan.liftscout.views.workout.records.RecordsPresenter;
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
    void inject(IntroActivity activity);
    void inject(MainActivity activity);

    // Fragments
    void inject(HomePresenter presenter);
    void inject(TodayPresenter presenter);

    // Intro
    void inject(IntroSettingsSlidePresenter presenter);
    void inject(IntroExercisesSlidePresenter presenter);
    void inject(IntroThemeSlidePresenter presenter);

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

    // Workout
    void inject(WorkoutContainerPresenter presenter);
    void inject(TrackerFragment fragment);
    void inject(TrackerPresenter presenter);
    void inject(HistoryPresenter presenter);
    void inject(GraphPresenter presenter);
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
    void inject(RecordRepoImpl repoImpl);

}
