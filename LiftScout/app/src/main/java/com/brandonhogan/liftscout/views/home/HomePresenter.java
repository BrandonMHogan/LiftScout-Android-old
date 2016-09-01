package com.brandonhogan.liftscout.views.home;

import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.core.managers.UserManager;
import com.brandonhogan.liftscout.injection.components.Injector;

import java.util.Date;

import javax.inject.Inject;

public class HomePresenter implements HomeContract.Presenter {


    // Injections
    //
    @Inject
    ProgressManager progressManager;

    @Inject
    UserManager userManager;


    // Private Properties
    //
    private HomeContract.View view;


    // Constructor
    //
    public HomePresenter(HomeContract.View view) {
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
