package com.brandonhogan.liftscout.interfaces.contracts;

import java.util.Date;

public interface TodayContainerContract {

    interface View {
    }

    interface Presenter {
        void viewCreated();
        void updateTodayProgress(Date currentDate);
        void saveWeight(double weight);
        double getWeight();
        String getTransformValue();

    }
}
