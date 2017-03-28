package com.brandonhogan.liftscout.presenters;

import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.TodayContainerContract;
import com.brandonhogan.liftscout.managers.ProgressManager;
import com.brandonhogan.liftscout.managers.UserManager;

import java.util.Date;

import javax.inject.Inject;

public class TodayContainerPresenter implements TodayContainerContract.Presenter {


    // Injections
    //
    @Inject
    ProgressManager progressManager;

    @Inject
    UserManager userManager;


    // Private Properties
    //
    private TodayContainerContract.View view;


    // Constructor
    //
    public TodayContainerPresenter(TodayContainerContract.View view) {
        Injector.getAppComponent().inject(this);
        this.view = view;
    }


    // Contracts
    //
    @Override
    public void viewCreated() {

    }

    @Override
    public void updateTodayProgress(Date currentDate) {
        progressManager.setTodayProgress(currentDate);
    }

    @Override
    public void saveWeight(double weight) {

    }

    @Override
    public double getWeight() {
        double weight;
        if (progressManager.getTodayProgress().getWeight() == 0)
            weight = userManager.getWeight();
        else
            weight = progressManager.getTodayProgress().getWeight();

        return weight;
    }

    @Override
    public String getTransformValue() {
        return userManager.getTransformValue();
    }
}
