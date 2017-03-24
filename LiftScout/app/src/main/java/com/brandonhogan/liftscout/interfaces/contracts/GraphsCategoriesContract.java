package com.brandonhogan.liftscout.interfaces.contracts;

import com.brandonhogan.liftscout.repository.model.CategoryGraph;

import java.util.ArrayList;

/**
 * Created by Brandon on 2/27/2017.
 * Description :
 */

public interface GraphsCategoriesContract {

    interface View {
        void setPieChart(ArrayList<CategoryGraph> categories, int total, long startDate, long endDate);
        void populateGraphType(ArrayList<Integer> types, int position);
    }

    interface Presenter {
        void viewCreated();
        void onGraphTypeSelected(int position);
    }
}
