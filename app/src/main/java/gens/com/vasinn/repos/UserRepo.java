package gens.com.vasinn.repos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ægir Már Jónsson on 5.5.2015.
 */
public class UserRepo {
    private List<String> users = new ArrayList<String>(7);

    public UserRepo() {
        users.add("aegir");
        users.add("liljar");
        users.add("gudjon");
        users.add("gudny");
        users.add("karl");
        users.add("trausti");
        users.add("gretar");
    }

    public List<String> getUsers() {
        return users;
    }
}