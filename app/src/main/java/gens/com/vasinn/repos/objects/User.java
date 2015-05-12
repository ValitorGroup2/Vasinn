package gens.com.vasinn.repos.objects;

import gens.com.vasinn.constants.UserConstants;

/**
 * Created by Ægir Már Jónsson on 6.5.2015.
 * and edited by Gudjon
 */
public class User extends Object {

    private String username;
    private String password;
    private String email;
    private int accessLevel;

    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public String getEmail()    {return email;}
    public int getAccessLevel() {return accessLevel;}

    private void set(String usrn, String pswd, String email, int accessLevel) {
        this.username   = usrn;
        this.password   = pswd;
        this.email      = email;
        this.accessLevel= accessLevel;
    }
    /* Constructors */
    public User(String usrn, String pswd) {
        this.set(usrn, pswd, "", UserConstants.USER_LEVEL_NORMAL);
    }

    public User(String usrn, String pswd, String email, int accessLevel) {
        this.set(usrn, pswd, email, accessLevel);
    }

    /* Only compering username and password because UserController::loginUser
        uses List::contains,  which uses this function to test if elements are equal
    * */
    @Override
    public boolean equals(Object obj) {
        User my  = this;
        User his = (User) obj;

        //we are not comparing the email
        return my.username.equals(his.username) &&
                my.password.equals(his.password);
    }
}
