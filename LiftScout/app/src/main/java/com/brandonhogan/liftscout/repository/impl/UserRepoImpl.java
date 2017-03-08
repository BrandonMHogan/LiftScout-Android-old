package com.brandonhogan.liftscout.repository.impl;

import com.brandonhogan.liftscout.core.model.User;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.DatabaseRealm;
import com.brandonhogan.liftscout.repository.UserRepo;

import java.util.Date;

import javax.inject.Inject;

public class UserRepoImpl implements UserRepo {

    @Inject
    DatabaseRealm databaseRealm;

    public UserRepoImpl() {
        Injector.getAppComponent().inject(this);
    }

    @Override
    public User getUser() {
        return databaseRealm.getRealmInstance().where(User.class).findFirst();
    }

    @Override
    public void setLastUsed(Date date) {

        databaseRealm.getRealmInstance().beginTransaction();
        User user = databaseRealm.getRealmInstance().where(User.class).findFirst();
        user.setLastUsed(date);
        databaseRealm.getRealmInstance().copyToRealmOrUpdate(user);
        databaseRealm.getRealmInstance().commitTransaction();
    }

    @Override
    public void setUser(User user) {
        databaseRealm.getRealmInstance().beginTransaction();
        databaseRealm.getRealmInstance().copyToRealmOrUpdate(user);
        databaseRealm.getRealmInstance().commitTransaction();
    }

    @Override
    public void setUserName(String name) {
        throw new UnsupportedOperationException("Setting username is not yet implemented");
    }

    @Override
    public void setWeight(double weight) {
        throw new UnsupportedOperationException("Setting weight is not yet implemented");
    }
}
