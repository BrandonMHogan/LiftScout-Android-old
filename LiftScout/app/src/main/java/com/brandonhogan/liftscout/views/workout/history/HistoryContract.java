package com.brandonhogan.liftscout.views.workout.history;

import java.util.Date;
import java.util.List;

public interface HistoryContract {

    interface View {
        void setupAdapter(List<HistoryListSection> data);
        void setupTitle(long start, long end);
        String getEmptySetMessage();
        void editTracker(int exerciseId);
    }

    interface Presenter {
        void viewCreated();
        void update();
        void setClicked(int exerciseId, Date date);
    }
}
