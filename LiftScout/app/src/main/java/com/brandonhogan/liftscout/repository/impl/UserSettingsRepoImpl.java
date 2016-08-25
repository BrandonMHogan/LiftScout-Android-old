package com.brandonhogan.liftscout.repository.impl;

import android.util.Log;

import com.brandonhogan.liftscout.core.model.UserSetting;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.DatabaseRealm;
import com.brandonhogan.liftscout.repository.UserSettingsRepo;

import javax.inject.Inject;

import io.realm.exceptions.RealmException;

public class UserSettingsRepoImpl implements UserSettingsRepo {

    private static final String TAG = "UserSettingsRepoImpl";

    @Inject
    DatabaseRealm databaseRealm;

    public UserSettingsRepoImpl() {
        Injector.getAppComponent().inject(this);
    }

    @Override
    public UserSetting getUserSetting(String type) {
        return databaseRealm.getRealmInstance().where(UserSetting.class)
                .equalTo(UserSetting.NAME, type).findFirst();
    }

    @Override
    public UserSetting createUserSetting(String type, String value) {

        try {
            databaseRealm.getRealmInstance().beginTransaction();
            UserSetting setting = new UserSetting();
            setting.setName(type);
            setting.setValue(value);
            databaseRealm.getRealmInstance().copyToRealmOrUpdate(setting);
            databaseRealm.getRealmInstance().commitTransaction();
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            return null;
        }

        return getUserSetting(type);
    }

    @Override
    public boolean setUserSetting(String type, String value) {

        try {
            databaseRealm.getRealmInstance().beginTransaction();

            databaseRealm.getRealmInstance().where(UserSetting.class)
                    .equalTo(UserSetting.NAME, type).findFirst().setValue(value);

            databaseRealm.getRealmInstance().commitTransaction();
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean deleteUserSetting(String type) {
        throw new UnsupportedOperationException("Delete User Setting is not yet implemented");
    }
}
