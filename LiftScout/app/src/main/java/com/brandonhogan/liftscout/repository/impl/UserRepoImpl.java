package com.brandonhogan.liftscout.repository.impl;

import com.brandonhogan.liftscout.core.model.User;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.DatabaseRealm;
import com.brandonhogan.liftscout.repository.UserRepo;

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
    public void setUserName(String name) {

    }

    @Override
    public void setWeight(double weight) {

    }
}
