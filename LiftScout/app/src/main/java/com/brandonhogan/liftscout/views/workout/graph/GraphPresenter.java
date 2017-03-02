package com.brandonhogan.liftscout.views.workout.graph;

import com.brandonhogan.liftscout.core.constants.Charts;
import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.core.managers.UserManager;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.core.utils.BhDate;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import io.realm.RealmResults;

/**
 * Created by Brandon on 2/15/2017.
 * Description :
 */

public class GraphPresenter implements GraphContract.Presenter {

    // Injections
    //
    @Inject
    ProgressManager progressManager;

    @Inject
    UserManager userManager;

    // Private Properties
    //
    private GraphContract.View view;
    private int exerciseId;


    // Constructor
    //
    public GraphPresenter(GraphContract.View view, int exerciseId) {
        Injector.getAppComponent().inject(this);

        this.view = view;
        this.exerciseId = exerciseId;
    }

    @Override
    public void viewCreated() {
    }

}
