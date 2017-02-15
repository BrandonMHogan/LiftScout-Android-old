package com.brandonhogan.liftscout.views.workout.graph;

import com.brandonhogan.liftscout.views.workout.history.HistoryListSection;

import java.util.List;

/**
 * Created by Brandon on 2/15/2017.
 * Description :
 */

public interface GraphContract {

    interface View {
        void setGraph(List<GraphDataSet> data);
    }

    interface Presenter {
        void viewCreated();
        void update();
    }
}
