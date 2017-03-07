package com.brandonhogan.liftscout.views.workout;

import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.core.managers.UserManager;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.ExerciseRepo;
import com.brandonhogan.liftscout.repository.impl.ExerciseRepoImpl;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Timed;

/**
 * Created by Brandon on 3/6/2017.
 * Description :
 */

public class WorkoutContainerPresenter implements WorkoutContainerContract.Presenter {


    // Injections
    //
    @Inject
    ProgressManager progressManager;

    @Inject
    UserManager userManager;

    // Private Properties
    //
    private WorkoutContainerContract.View view;
    private int exerciseId;
    private ExerciseRepo exerciseRepo;
    private int exerciseTimer, exerciseTimerTracked;
    private boolean exerciseTrackVibrate;

    private Disposable disposable;


    // Constructor
    //
    public WorkoutContainerPresenter(WorkoutContainerContract.View view, int exerciseId) {
        Injector.getAppComponent().inject(this);

        exerciseRepo = new ExerciseRepoImpl();

        this.view = view;
        this.exerciseId = exerciseId;
    }

    @Override
    public void viewCreated() {

    }

    @Override
    public int getExerciseId() {
        return exerciseId;
    }

    @Override
    public String getExerciseName() {
        return exerciseRepo.getExercise(exerciseId).getName();
    }

    @Override
    public void onDeleteSet() {
        Set set = progressManager.getTodayProgressSet(exerciseId);

        progressManager.deleteSet(set);
        progressManager.clearUpdatedSet();
        view.deleteSetSuccess();
    }

    @Override
    public void onSettingsSave(int timerValue, boolean vibrate) {
        exerciseRepo.setExerciseRestTimer(exerciseId, timerValue);
        exerciseRepo.setExerciseRestVibrate(exerciseId, vibrate);
        exerciseTimer = timerValue;
    }

    @Override
    public int getExerciseRestTimer() {
        if (exerciseTimer == 0)
            exerciseTimer = exerciseRepo.getExerciseRestTimer(exerciseId);

        return exerciseTimer;
    }

    @Override
    public boolean getExerciseRestVibrate() {
        exerciseTrackVibrate = exerciseRepo.getExerciseRestVibrate(exerciseId);
        return exerciseTrackVibrate;
    }

    @Override
    public void onTimerClicked() {
        exerciseTimerTracked = getExerciseRestTimer() + 1;

        disposable = io.reactivex.Observable.interval(0, 1, TimeUnit.SECONDS)
                .timeInterval()
                .observeOn(AndroidSchedulers.mainThread())
                .take(exerciseTimerTracked)
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        view.onRestTimerTerminate(getExerciseRestVibrate());
                        disposable.dispose();
                    }
                })
                .subscribe(new Consumer<Timed<Long>>() {
                    @Override
                    public void accept(@NonNull Timed<Long> longTimed) throws Exception {
                        exerciseTimerTracked -= 1;
                        view.onRestTimerTick(exerciseTimerTracked);
                    }
                });
    }

    @Override
    public void onRestTimerStop() {
        if (disposable != null)
            disposable.dispose();
    }
}
