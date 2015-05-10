package gens.com.vasinn.repos.objects;

/**
 * Created by Ægir Már Jónsson on 6.5.2015.
 */
public class User extends Object {

    private String username;
    private String password;
    private String email;

    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public String getEmail()    {return email;}

    private void set(String usrn, String pswd, String email) {
        this.username = usrn;
        this.password = pswd;
        this.email    = email;
    }
    /* Constructors */
    public User(String usrn, String pswd) {
        this.set(usrn, pswd, "");
    }

    public User(String usrn, String pswd, String email) {
        this.set(usrn, pswd, email);
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
