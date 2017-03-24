package com.brandonhogan.liftscout.interfaces.contracts;

import com.brandonhogan.liftscout.models.TrackerListModel;

import java.util.Date;
import java.util.List;

public interface TrackerContract {

    interface View {
        void updateAdapter(List<TrackerListModel> data);
        void setDate(Date date);
        void updateValues(float weight, int reps);
        String getRepsLabel(boolean isMultiple);
        void saveSuccess(int position, boolean isNew);
        void onDelete(int position);
        void deleteSetSuccess();
        void clear(boolean clearValues, TrackerListModel model);
        void onSelect(TrackerListModel rep);
        void updateIncrements(double increment);
    }

    interface Presenter {
        void viewCreated();
        void updateAdapter();
        void onSave(String reps, String weight);
        void onDeleteRep(int position);
        void onSelect(int position);
        void onButtonTwoPressed();
        void updateIncrement();
    }
}
