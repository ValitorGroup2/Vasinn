package gens.com.vasinn.repos.objects;

/**
 * Created by Ægir Már Jónsson on 6.5.2015.
 */
public class User extends Object {

    private String username;
    private String password;

    /* Constructor */
    public User(String usrn, String pswd) {
        this.username = usrn;
        this.password = pswd;
    }

    @Override
    public boolean equals(Object obj) {
        User my  = this;
        User his = (User) obj;

        if (my.username.equals(his.username) &&
            my.password.equals(his.password))
            return true;
        else
            return false;
    }
}
