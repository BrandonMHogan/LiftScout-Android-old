package com.brandonhogan.liftscout.views.settings.profile;

import java.util.ArrayList;

/**
 * Created by Brandon on 2/17/2017.
 * Description :
 */

public class SettingsProfileContract {
    interface View {
        void populateMeasurements(ArrayList<String> measurements, int position);
        void saveSuccess(int msg);
    }

    interface Presenter {
        void viewCreated();
        void onMeasurementSelected(int position);
    }
}
