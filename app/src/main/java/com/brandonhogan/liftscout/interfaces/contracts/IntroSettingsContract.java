package com.brandonhogan.liftscout.interfaces.contracts;

import com.brandonhogan.liftscout.presenters.base.PresenterBase;

import java.util.ArrayList;

/**
 * Created by Brandon on 2/26/2017.
 * Description :
 */

public interface IntroSettingsContract {

    interface View {
        void populateHomeDefaults(ArrayList<String> screens, int position);
        void populateMeasurementDefaults(ArrayList<String> screens, int position);
    }

    interface Presenter extends PresenterBase {
        void setView(IntroSettingsContract.View view);
        void onHomeDefaultSelected(int position);
        void onMeasurementDefaultSelected(int position);
    }
}
