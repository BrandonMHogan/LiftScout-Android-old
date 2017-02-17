package com.brandonhogan.liftscout.core.managers;

import com.brandonhogan.liftscout.core.constants.Measurements;
import com.brandonhogan.liftscout.core.constants.Themes;
import com.brandonhogan.liftscout.core.constants.TodayTransforms;
import com.brandonhogan.liftscout.core.model.User;
import com.brandonhogan.liftscout.core.model.UserSetting;
import com.brandonhogan.liftscout.repository.UserRepo;
import com.brandonhogan.liftscout.repository.UserSettingsRepo;
import com.brandonhogan.liftscout.repository.impl.UserRepoImpl;
import com.brandonhogan.liftscout.repository.impl.UserSettingsRepoImpl;

public class UserManager {

    // Private Static Properties
    //
    private static final String TAG = "UserManager";


    // Private Properties
    //
    private User user;
    private UserRepo userRepo;
    private UserSettingsRepo userSettingsRepo;

    public UserManager() {
        userRepo = new UserRepoImpl();
        userSettingsRepo = new UserSettingsRepoImpl();
        user = userRepo.getUser();
    }

    public boolean validUser() {
        user = userRepo.getUser();
        return user != null;
    }

    public User getUser() {
        user = userRepo.getUser();
        return user;
    }

    public void setUser(User user) {
        userRepo.setUser(user);
        this.user = user;
    }

    public String getName() {
        return user.getName();
    }

    public double getWeight() {
        return user.getWeight();
    }

    public void setName(String name) {
        userRepo.setUserName(name);
        user = userRepo.getUser();
    }

    public void setWeight(double weight) {
        userRepo.setWeight(weight);
        user = userRepo.getUser();
    }

    public String getTransformValue() {
        UserSetting setting = userSettingsRepo.getUserSetting(UserSetting.TODAY_TRANSFORM);

        if (setting == null) {
            setting = userSettingsRepo.createUserSetting(UserSetting.TODAY_TRANSFORM, TodayTransforms.DEFAULT);
        }

        return setting.getValue();
    }

    public boolean setTransform(String value) {
        return userSettingsRepo.setUserSetting(UserSetting.TODAY_TRANSFORM, value);
    }

    public String getThemeValue() {
        UserSetting setting = userSettingsRepo.getUserSetting(UserSetting.THEME);

        if (setting == null) {
            setting = userSettingsRepo.createUserSetting(UserSetting.THEME, Themes.DARK);
        }

        return setting.getValue();
    }

    public boolean setTheme(String value) {
        return userSettingsRepo.setUserSetting(UserSetting.THEME, value);
    }

    public String getMeasurementValue() {
        UserSetting setting = userSettingsRepo.getUserSetting(UserSetting.MEASUREMENT);

        if (setting == null) {
            setting = userSettingsRepo.createUserSetting(UserSetting.MEASUREMENT, Measurements.POUNDS);
        }

        return setting.getValue();
    }

    public boolean setMeasurement(String value) {
        return userSettingsRepo.setUserSetting(UserSetting.MEASUREMENT, value);
    }
}
