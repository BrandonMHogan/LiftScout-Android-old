package com.brandonhogan.liftscout.interfaces.contracts;


import com.brandonhogan.liftscout.models.TodayListSectionModel;

import java.util.List;

public interface TodayContract {
    interface View {
        void setupTitle(int titleRes, String titleDate);
        void setupWeight(String weight);
        void setupAdapter(List<TodayListSectionModel> data, int expandPosition);
        String getEmptySetMessage();
        void onSetDeleted(int position, int count);
    }

    interface Presenter {
        void viewCreate();
        void update();
        void onDeleteSection(TodayListSectionModel section, int position);
    }
}
