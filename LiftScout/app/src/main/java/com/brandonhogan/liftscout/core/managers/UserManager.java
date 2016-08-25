package com.brandonhogan.liftscout.core.managers;

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

    public UserSetting getTodayTransformSetting() {
        UserSetting setting = userSettingsRepo.getUserSetting(UserSetting.TODAY_TRANSFORM);

        if (setting == null) {
            setting = userSettingsRepo.createUserSetting(UserSetting.TODAY_TRANSFORM, TodayTransforms.DEFAULT);
        }

        return setting;
    }

    public UserSetting getTheme() {
        UserSetting setting = userSettingsRepo.getUserSetting(UserSetting.THEME);

        if (setting == null) {
            setting = userSettingsRepo.createUserSetting(UserSetting.THEME, Themes.DARK);
        }

        return setting;
    }

    public boolean setTheme(String value) {
        return userSettingsRepo.setUserSetting(UserSetting.THEME, value);
    }
}
