package gens.com.vasinn.repos;

import java.util.ArrayList;
import java.util.List;

import gens.com.vasinn.repos.objects.User;

/**
 * Created by Ægir Már Jónsson on 5.5.2015.
 */
public class UserRepo {
    private List<User> users = new ArrayList<>(8);

    public UserRepo() {
        users.add(new User("aegir",   "123"));
        users.add(new User("liljar",  "123"));
        users.add(new User("gudjon",  "123"));
        users.add(new User("gudny",   "123"));
        users.add(new User("karl",    "123"));
        users.add(new User("trausti", "123"));
        users.add(new User("gretar",  "123"));

        // TODO DELETE THIS SHIT BEFORE THE SÝNING s
        users.add(new User("", ""));
    }

    public List<User> getUsers() {
        return users;
    }
}