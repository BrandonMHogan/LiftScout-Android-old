package com.brandonhogan.liftscout.views.home.today;


import java.util.List;

public interface TodayContact {
    interface View {
        void setupTitle(int titleRes, String titleDate);
        void setupWeight(String weight);
        void setupAdapter(List<TodayListSection> data, int expandPosition);
        String getEmptySetMessage();
        void onSetDeleted(int position, int count);
    }

    interface Presenter {
        void viewCreate();
        void update();
        void onDeleteSection(TodayListSection section, int position);
    }
}
