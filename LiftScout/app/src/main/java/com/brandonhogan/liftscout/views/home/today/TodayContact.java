package com.brandonhogan.liftscout.views.home.today;

import java.util.List;

public interface TodayContact {
    interface View {
        void setupTitle(String date, String year);
        void setupWeight(String weight);
        void setupAdapter(List<TodayListSection> data, int expandPosition);
    }

    interface Presenter {
        void viewCreate();
        void update();
        void itemTouchOnMove(int oldId, int newId);
    }
}
