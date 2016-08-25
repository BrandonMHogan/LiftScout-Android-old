package com.brandonhogan.liftscout.core.managers;

import com.brandonhogan.liftscout.core.model.User;
import com.brandonhogan.liftscout.repository.UserRepo;
import com.brandonhogan.liftscout.repository.impl.UserRepoImpl;

import javax.inject.Inject;

public class UserManager {

    // Private Static Properties
    //
    private static final String TAG = "UserManager";


    // Private Properties
    //
    private User user;
    private UserRepo userRepo;

    public UserManager() {
        userRepo = new UserRepoImpl();
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

}
