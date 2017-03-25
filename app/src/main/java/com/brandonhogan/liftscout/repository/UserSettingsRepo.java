package com.brandonhogan.liftscout.repository;

import com.brandonhogan.liftscout.repository.model.UserSetting;

public interface UserSettingsRepo {

    UserSetting getUserSetting(String type);
    UserSetting createUserSetting(String type, String value);
    boolean setUserSetting(String type, String value);
    boolean deleteUserSetting(String type);
}
