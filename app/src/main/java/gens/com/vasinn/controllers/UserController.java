package gens.com.vasinn.controllers;

import java.util.List;

import gens.com.vasinn.repos.UserRepo;
import gens.com.vasinn.repos.objects.User;

/**
 * Created by Ægir Már Jónsson on 5.5.2015.
 */
public class UserController {
    private UserRepo userRepo;

    public UserController() {
        userRepo = new UserRepo();
    }

    /* Checks if user can login, returns true if so, else false */
    public boolean loginUser(String username, String password) {
        List<User> users = userRepo.getUsers();
        if (users.contains(new User(username, password)))   return true;
        else                                                return false;
    }
}
