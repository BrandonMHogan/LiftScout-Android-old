package com.brandonhogan.liftscout.interfaces.contracts;

import com.brandonhogan.liftscout.presenters.base.PresenterBase;

import java.util.ArrayList;

public interface SettingsHomeContract {

    interface View {
        void populateTransforms(ArrayList<String> themes, int position);
        void populateHomeDefaults(ArrayList<String> screens, int position);
        void saveSuccess(int message);
    }

    interface Presenter extends PresenterBase {
        void setView(SettingsHomeContract.View view);
        void onTransformSelected(int position);
        void onHomeDefaultSelected(int position);
    }

}
