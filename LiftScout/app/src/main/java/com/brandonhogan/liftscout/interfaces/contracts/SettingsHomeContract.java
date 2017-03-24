package com.brandonhogan.liftscout.interfaces.contracts;

import java.util.ArrayList;

public interface SettingsHomeContract {

    interface View {
        void populateTransforms(ArrayList<String> themes, int position);
        void populateHomeDefaults(ArrayList<String> screens, int position);
        void saveSuccess(int message);
    }

    interface Presenter {
        void viewCreated();
        void onTransformSelected(int position);
        void onHomeDefaultSelected(int position);
    }

}
