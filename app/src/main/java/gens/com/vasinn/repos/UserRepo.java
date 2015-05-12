package gens.com.vasinn.repos;

import java.util.ArrayList;
import java.util.List;

import gens.com.vasinn.constants.UserConstants;
import gens.com.vasinn.repos.objects.Transaction;
import gens.com.vasinn.repos.objects.User;

/**
 * Created by Ægir Már Jónsson on 5.5.2015.
 */
public class UserRepo {
    private List<User> users = new ArrayList<>();

    public UserRepo() {
        users.add(new User("aegir",   "123", "aegir13@ru.is", UserConstants.USER_LEVEL_NORMAL));
        users.add(new User("liljar",  "123", "liljar11@ru.is", UserConstants.USER_LEVEL_NORMAL));
        users.add(new User("gudjon",  "123", "gudjon02@ru.is", UserConstants.USER_LEVEL_NORMAL));
        users.add(new User("gudny",   "123", "gudnyoskg@gmail.com", UserConstants.USER_LEVEL_NORMAL));
        users.add(new User("karl",    "123", "karlb12@ru.is", UserConstants.USER_LEVEL_NORMAL));
        users.add(new User("trausti", "123", "traustie09@ru.is", UserConstants.USER_LEVEL_NORMAL));
        users.add(new User("gretar",  "123", "grellinn@gmail.com", UserConstants.USER_LEVEL_NORMAL));
    }

    public List<User> getUsers() {
        return users;
    }
    /**
     * Created by Gudjon on 9.5.2015.
     */
    public User get(int index){

        return users.get(index);
    }
    /**
     * Created by Gudjon on 9.5.2015.
     * returns null if userName is not found created by Gudjon 9.5.2015
     */
    public User findByName(String userNameToFind){

        User ret;
        for(int i = 0; i < users.size(); i++) {
            ret = users.get(i);
            if (ret.getUsername().equals(userNameToFind))
                return ret;
        }
        return null;
    }
    /**
     * Created by Gudjon on 9.5.2015.
     * returns null if userEmail is not found created by Gudjon 9.5.2015
     */
    public User findByEmail(String userEmailToFind){

        User ret;
        for(int i = 0; i < users.size(); i++) {
            ret = users.get(i);
            if (ret.getEmail().equals(userEmailToFind))
                return ret;
        }
        return null;
    }
}