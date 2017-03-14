package com.brandonhogan.liftscout.views.workout.records;

import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.core.managers.UserManager;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.RecordsRepo;

import javax.inject.Inject;

/**
 * Created by Brandon on 3/13/2017.
 * Description :
 */

public class RecordsPresenter implements RecordsContract.Presenter {


    // Injections
    //
    @Inject
    ProgressManager progressManager;

    @Inject
    UserManager userManager;


    // Private Properties
    //
    private RecordsContract.View view;
    private int exerciseId;
    private RecordsRepo recordsRepo;


    // Constructor
    //
    public RecordsPresenter(RecordsContract.View view, int exerciseId) {
        Injector.getAppComponent().inject(this);

        this.view = view;
        this.exerciseId = exerciseId;
    }


    // Contracts

    @Override
    public void viewCreated() {

//        recordsRepo = new RecordRepoImpl();
//        recordsRepo.createRecord();
//
//        Record pr = recordsRepo.getPersonalRecord();
//
//        Log.d("Whatever", "erewrwer");

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }
}
