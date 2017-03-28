package com.brandonhogan.liftscout.managers;

import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.CategoryRepo;
import com.brandonhogan.liftscout.repository.DatabaseRealm;
import com.brandonhogan.liftscout.repository.ExerciseRepo;
import com.brandonhogan.liftscout.repository.ProgressRepo;
import com.brandonhogan.liftscout.repository.SetRepo;
import com.brandonhogan.liftscout.repository.impl.CategoryRepoImpl;
import com.brandonhogan.liftscout.repository.impl.ExerciseRepoImpl;
import com.brandonhogan.liftscout.repository.impl.ProgressRepoImpl;
import com.brandonhogan.liftscout.repository.impl.SetRepoImpl;
import com.brandonhogan.liftscout.repository.model.Progress;
import com.brandonhogan.liftscout.repository.model.Rep;
import com.brandonhogan.liftscout.repository.model.Set;
import com.brandonhogan.liftscout.utils.BhDate;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ProgressManager {


    // Injectors
    //
    @Inject
    DatabaseRealm databaseRealm;

    @Inject
    RecordsManager recordsManager;


    // Private Static Properties
    //
    private static final String TAG = "ProgressManager";


    // Private Properties
    //
    private ProgressRepo progressRepo;
    private SetRepo setRepo;
    private CategoryRepo categoryRepo;
    private ExerciseRepo exerciseRepo;

    private Progress todayProgress;
    private Set updatedSet;


    // Constructor
    //
    public ProgressManager() {
        Injector.getAppComponent().inject(this);

        progressRepo = new ProgressRepoImpl();
        setRepo = new SetRepoImpl();
        categoryRepo = new CategoryRepoImpl();
        exerciseRepo = new ExerciseRepoImpl();

        // Will default to today
        setTodayProgress(BhDate.trimTimeFromDate(new Date()));
    }


    // Today Progress
    /*
        Will keep a reference of the current days progress object and allow for quick
        access to it
     */
    public Progress getTodayProgress() {
        return todayProgress;
    }

    public void setTodayProgress(Progress mTodayProgress) {
        this.todayProgress = mTodayProgress;
        progressRepo.setProgress(todayProgress);
    }

    public void setTodayProgress(Date date) {

        date = BhDate.trimTimeFromDate(date);

        todayProgress = databaseRealm.getRealmInstance()
                .where(Progress.class)
                .equalTo(Progress.DATE, date)
                .findFirst();

        if (todayProgress == null) {
            todayProgress = new Progress();
            todayProgress.setDate(date);
            todayProgress.setSets(new RealmList<Set>());

            progressRepo.setProgress(todayProgress);

            todayProgress = progressRepo.getProgress(todayProgress.getId());
        }
    }

    public void setTodayProgress(long date) {
        setTodayProgress(new Date(date));
    }

    public Set getTodayProgressSet(int exerciseId) {
        Set set = todayProgress.getSets().where().equalTo("exercise.id", exerciseId).findFirst();

        if (set == null) {
            Number orderNum = todayProgress.getSets().where().max(Set.ORDER_ID);
            int order = 0;

            if (orderNum != null)
                order = orderNum.intValue() + 1;

            set = new Set();
            set.setExercise(exerciseRepo.getExercise(exerciseId));
            set.setDate(todayProgress.getDate());
            set.setReps(new RealmList<Rep>());
            set.setOrderId(order);

            setRepo.addSet(todayProgress, set);
        }

        return set;
    }

    public Date getCurrentDate() {
        return todayProgress.getDate();
    }

    // Sets

    public void updateTodayProgressSet(Set set) {
        setRepo.updateSet(set);
    }

    public RealmList<Set> getSetsByDate(Date date) {
        Progress progress = progressRepo.getProgress(date);

        if (progress == null)
            return null;
        else
            return progress.getSets();
    }

    public RealmResults<Set> getSetsByExercise(int exerciseId) {
        return setRepo.getSets(exerciseId);
    }

    public RealmResults<Set> getAllSets() {
        return setRepo.getSets();
    }

    public void deleteSet(final Set set) {
        recordsManager.setDeleted(set.getId())
        .subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                setRepo.deleteSet(set);
            }
        });
    }

    public void deleteSet(final int setId) {
        recordsManager.setDeleted(setId)
        .subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                setRepo.deleteSet(setId);
            }
        });
    }

    public Set getPreviousSet(int exerciseId) {
        return setRepo.getPreviousSet(todayProgress.getDate(), exerciseId);
    }


    // Reps

    public void addRepToTodayProgress(final Set set, final Rep rep) {
        setRepo.addRep(set, rep)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        recordsManager.repCreated(rep.getId(), set.getExercise().getId())
                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(@NonNull Boolean aBoolean) throws Exception {

                                    }
                                });
                    }
                });
    }

    public void updateRep(Rep rep, int exerciseId) {
        setRepo.updateRep(rep);

        recordsManager.repUpdated(rep.getId(), exerciseId)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {

                    }
                });
    }

    public void deleteRep(final int repId, final int exerciseId) {

        recordsManager.repDeleted(repId, exerciseId)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        setRepo.deleteRep(repId);
                    }
                });
    }


    // Set Updater
    /*
        When a set is edited, and we need to track it back to the home fragments
     */
    public boolean isSetUpdated() {
        return updatedSet != null;
    }

    public Set getUpdatedSet() {
        return updatedSet;
    }

    public void updateSet(Set set) {
        updatedSet = set;
    }

    public void clearUpdatedSet() {
        updatedSet = null;
    }
}
