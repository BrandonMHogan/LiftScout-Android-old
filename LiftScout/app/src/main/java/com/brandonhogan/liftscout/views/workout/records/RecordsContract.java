package com.brandonhogan.liftscout.views.workout.records;

/**
 * Created by Brandon on 3/13/2017.
 * Description :
 */

public interface RecordsContract {
    interface View {
    }

    interface Presenter {
        void viewCreated();
        void onResume();
        void onPause();
        void onDestroy();
    }
}
