package com.brandonhogan.liftscout.views.workout.tracker;

import java.util.Date;
import java.util.List;

public interface TrackerContract {

    interface View {
        void updateAdapter(List<TrackerListModel> data);
        void setDate(Date date);
        void updateValues(float weight, int reps);
        String getRepsLabel(boolean isMultiple);
        void saveSuccess(int position);
        void showDeleteRepAlert();
        void deleteSetSuccess();
        void clear(boolean clearValues);
        void onSelect(TrackerListModel rep);
        void updateIncrements(double increment);
    }

    interface Presenter {
        void viewCreated();
        void updateAdapter();
        void onSave(String reps, String weight);
        void onDeleteRep();
        void onSelect(int position);
        void onButtonTwoPressed();
        void updateIncrement();
    }
}
