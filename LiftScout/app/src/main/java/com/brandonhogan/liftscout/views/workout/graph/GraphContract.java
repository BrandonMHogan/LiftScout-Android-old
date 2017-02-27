package com.brandonhogan.liftscout.views.workout.graph;

import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.data.Entry;

/**
 * Created by Brandon on 2/15/2017.
 * Description :
 */

public interface GraphContract {

    interface View {
        void setGraph(List<GraphDataSet> data, int uniqueDateCount);
        void setSelected(String date, String value);
        void populateGraphTypes(ArrayList<String> types, int position);
    }

    interface Presenter {
        void viewCreated(int position);
        void update();
        void update(int position);
        void onGraphTypeSelected(int position);
        void onItemSelected(Entry entry);
    }
}
