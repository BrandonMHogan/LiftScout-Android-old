package com.brandonhogan.liftscout.presenters;

import com.brandonhogan.liftscout.events.IncrementEvent;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.WorkoutContainerContract;
import com.brandonhogan.liftscout.managers.ProgressManager;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.repository.ExerciseRepo;
import com.brandonhogan.liftscout.repository.model.Exercise;
import com.brandonhogan.liftscout.repository.model.Set;
import com.brandonhogan.liftscout.utils.constants.ConstantValues;
import com.brandonhogan.liftscout.utils.constants.Measurements;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
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

    @Inject
    ExerciseRepo exerciseRepo;

    // Private Properties
    //
    private WorkoutContainerContract.View view;
    private int exerciseId;
    private Exercise exercise;
    private int exerciseTimer, exerciseTimerTracked;
    private ArrayList<Double> increments;

    private Disposable disposable;
    private Observer<Long> restObserver;


    // Constructor
    //
    public WorkoutContainerPresenter(WorkoutContainerContract.View view, int exerciseId) {
        Injector.getAppComponent().inject(this);

        this.view = view;
        this.exerciseId = exerciseId;
        this.exercise = exerciseRepo.getExercise(exerciseId);
        increments = ConstantValues.increments;

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
    public int getCategoryId() {
        return exercise.getCategoryId();
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
    public void onSettingsSave(int timerValue, boolean vibrate, boolean sound, boolean autoStart, int incrementIndex) {
        exerciseRepo.setExerciseRestTimer(exerciseId, timerValue);
        exerciseRepo.setExerciseRestVibrate(exerciseId, vibrate);
        exerciseRepo.setExerciseRestSound(exerciseId, sound);
        exerciseRepo.setExerciseRestAutoStart(exerciseId, autoStart);
        exerciseRepo.setExerciseIncrement(exerciseId, increments.get(incrementIndex));
        exerciseTimer = timerValue;

        EventBus.getDefault().post(new IncrementEvent());
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
        return exerciseRepo.getExerciseRestVibrate(exerciseId);
    }

    @Override
    public boolean getExerciseRestSound() {
        return exerciseRepo.getExerciseRestSound(exerciseId);
    }

    @Override
    public boolean getExerciseRestAutoStart() {
        return exerciseRepo.getExerciseRestAutoStart(exerciseId);
    }

    @Override
    public int getExerciseIncrementIndex() {
        if (!increments.contains(exerciseRepo.getExerciseIncrement(exerciseId))) {
            exerciseRepo.setExerciseIncrement(exerciseId, ConstantValues.increments.get(ConstantValues.increments.indexOf(getDefaultIncrement())));
        }

        return increments.indexOf(exerciseRepo.getExerciseIncrement(exerciseId));
    }

    @Override
    public ArrayList<Double> getExerciseIncrementList() {
        return increments;
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

        exerciseTimerTracked = 0;

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
                if (view != null) {
                    exerciseTimerTracked -= 1;
                    view.onRestTimerTick(exerciseTimerTracked);
                }

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                if (view != null) {
                    view.onRestTimerTerminate(getExerciseRestVibrate(), getExerciseRestSound());
                }
            }
        };
    }

    private double getDefaultIncrement() {
        if (userManager.getMeasurementValue().equals(Measurements.KILOGRAMS))
            return ConstantValues.INCREMENT_KG_DEFAULT;
        else
            return ConstantValues.INCREMENT_LB_DEFAULT;
    }

    private void runTimer() {
        io.reactivex.Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .take(exerciseTimerTracked)
                .subscribeWith(restObserver);
    }
}
