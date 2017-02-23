package com.brandonhogan.liftscout.views.settings.home;

import java.util.ArrayList;

public interface SettingsHomeContract {

    interface View {
        void populateTransforms(ArrayList<String> themes, int position);
        void populateHomeDefaults(ArrayList<String> screens, int position);
        void saveSuccess();
    }

    interface Presenter {
        void viewCreated();
        void onTransformSelected(int position);
        void onHomeDefaultSelected(int position);
        void onSave();
    }

}
