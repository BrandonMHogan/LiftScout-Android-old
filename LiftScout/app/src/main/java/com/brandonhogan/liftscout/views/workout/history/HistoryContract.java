package com.brandonhogan.liftscout.views.workout.history;

import java.util.List;

public interface HistoryContract {

    interface View {
        void setupAdapter(List<HistoryListSection> data);
        String getEmptySetMessage();
        void goToHome();
    }

    interface Presenter {
        void viewCreated();
        void update();
        void editEvent(HistoryTrackerEvent event);
    }
}
