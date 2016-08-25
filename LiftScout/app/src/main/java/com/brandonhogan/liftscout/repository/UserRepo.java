package com.brandonhogan.liftscout.repository;

import com.brandonhogan.liftscout.core.model.User;

public interface UserRepo {
    User getUser();
    void setUserName(String name);
    void setWeight(double weight);
}
