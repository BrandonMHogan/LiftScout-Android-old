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
        void showDeleteSetAlert();
        void showDeleteRepAlert();
        void deleteSetSuccess();
        void clear(boolean clearValues);
        void onSelect(TrackerListModel rep);
    }

    interface Presenter {
        void viewCreated();
        void updateAdapter();
        void onSave(String reps, String weight);
        void onDelete();
        void onDeleteRep();
        void onDeleteSet();
        void onSelect(int position);
        void onButtonTwoPressed();
        String getExerciseName();
    }
}