package com.brandonhogan.liftscout.views.home;

import java.util.Date;

public interface HomeContract {

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
