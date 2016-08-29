package com.brandonhogan.liftscout.views.workout.tracker;

import java.util.List;

public interface TrackerContract {

    interface View {
        void updateAdapter(List<TrackerListModel> data);
        String getRepsLabel(boolean isMultiple);
        void saveSuccess(int position);
    }

    interface Presenter {
        void viewCreated();
        void updateAdapter();
        void onSave(String reps, String weight);
    }

}
