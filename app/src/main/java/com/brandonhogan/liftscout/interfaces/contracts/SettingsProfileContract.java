package com.brandonhogan.liftscout.interfaces.contracts;

import com.brandonhogan.liftscout.presenters.base.PresenterBase;

import java.util.ArrayList;

/**
 * Created by Brandon on 2/17/2017.
 * Description :
 */

public interface SettingsProfileContract {
    interface View {
        void populateMeasurements(ArrayList<String> measurements, int position);
        void saveSuccess(int msg);
    }

    interface Presenter extends PresenterBase {
        void onMeasurementSelected(int position);
        void setView(SettingsProfileContract.View view);
    }
}
