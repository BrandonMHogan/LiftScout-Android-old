package com.brandonhogan.liftscout.views.workout;

import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.core.managers.UserManager;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.ExerciseRepo;
import com.brandonhogan.liftscout.repository.impl.ExerciseRepoImpl;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
    private Observer<Long> restObserver;


    // Constructor
    //
    public WorkoutContainerPresenter(WorkoutContainerContract.View view, int exerciseId) {
        Injector.getAppComponent().inject(this);

        exerciseRepo = new ExerciseRepoImpl();

        this.view = view;
        this.exerciseId = exerciseId;

        setupObserver();
    }

    @Override
    public void viewCreated() {

    }

    @Override
    public void onDestroyView() {
        this.view = null;
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
    public long getDateLong() {
        return progressManager.getCurrentDate().getTime();
    }

    @Override
    public void onDeleteSet() {
        Set set = progressManager.getTodayProgressSet(exerciseId);

        progressManager.deleteSet(set);
        progressManager.clearUpdatedSet();
        view.deleteSetSuccess();
    }

    @Override
    public void onSettingsSave(int timerValue, boolean vibrate, boolean sound) {
        exerciseRepo.setExerciseRestTimer(exerciseId, timerValue);
        exerciseRepo.setExerciseRestVibrate(exerciseId, vibrate);
        exerciseRepo.setExerciseRestSound(exerciseId, sound);
        exerciseTimer = timerValue;
    }

    @Override
    public int getRemainingRestTime() {
        return exerciseTimerTracked;
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
    public boolean getExerciseRestSound() {
        exerciseTrackVibrate = exerciseRepo.getExerciseRestSound(exerciseId);
        return exerciseTrackVibrate;
    }

    @Override
    public void onTimerClicked() {
        onRestTimerStop();
        exerciseTimerTracked = getExerciseRestTimer() + 1;
        runTimer();
    }

    @Override
    public void restTimerNotification(int time) {
        if(time > 0) {
            onRestTimerStop();
            exerciseTimerTracked = time;
            runTimer();
        }
    }

    @Override
    public void onRestTimerStop() {
        Schedulers.shutdown();
        if (disposable != null)
            disposable.dispose();
        Schedulers.start();
    }

    private void setupObserver() {

        restObserver = new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(Long aLong) {
                exerciseTimerTracked -= 1;
                view.onRestTimerTick(exerciseTimerTracked);

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                view.onRestTimerTerminate(getExerciseRestVibrate(), getExerciseRestSound());
            }
        };
    }

    private void runTimer() {
        io.reactivex.Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .take(exerciseTimerTracked)
                .subscribeWith(restObserver);
    }
}
