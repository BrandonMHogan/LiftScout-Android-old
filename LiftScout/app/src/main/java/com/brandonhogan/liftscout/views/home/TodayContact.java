package com.brandonhogan.liftscout.views.home;

import com.brandonhogan.liftscout.views.home.workout.WorkoutSection;

import java.util.List;

public interface TodayContact {
    interface View {
        void setupTitle(String date, String year);
        void setupWeight(String weight);
        void setupAdapter(List<WorkoutSection> data, int expandPosition);
    }

    interface Presenter {
        void viewCreate();
        void update();
        void itemTouchOnMove(int oldId, int newId);
    }
}
