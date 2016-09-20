package com.brandonhogan.liftscout.views.workout.history;

import java.util.List;

public interface HistoryContract {

    interface View {
        void setupAdapter(List<HistoryListSection> data);
        String getEmptySetMessage();
    }

    interface Presenter {
        void viewCreated();
    }
}
