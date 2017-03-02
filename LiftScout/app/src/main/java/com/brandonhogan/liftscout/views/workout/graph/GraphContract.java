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
    }

    interface Presenter {
        void viewCreated();
    }
}
