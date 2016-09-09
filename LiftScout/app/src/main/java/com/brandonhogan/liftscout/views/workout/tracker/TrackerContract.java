package com.brandonhogan.liftscout.views.workout.tracker;

import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;

import java.util.List;

public interface TrackerContract {

    interface View {
        void updateAdapter(List<TrackerListModel> data);
        String getRepsLabel(boolean isMultiple);
        void saveSuccess(int position);
        void deleteSuccess();
        void onSelect(TrackerListModel rep);
    }

    interface Presenter {
        void viewCreated();
        void updateAdapter();
        void onSave(String reps, String weight);
        void onDelete();
        void onSelect(int position);
        void onButtonTwoPressed();
        void onDeleteRep();
        String getExerciseName();
    }
}
