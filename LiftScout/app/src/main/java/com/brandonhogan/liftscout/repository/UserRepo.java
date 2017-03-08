package com.brandonhogan.liftscout.repository;

import com.brandonhogan.liftscout.core.model.User;

import java.util.Date;

public interface UserRepo {
    User getUser();
    void setLastUsed(Date date);
    void setUser(User user);
    void setUserName(String name);
    void setWeight(double weight);
}
