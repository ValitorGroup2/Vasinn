package gens.com.vasinn;

import android.app.Application;
import android.content.SharedPreferences;

import gens.com.vasinn.controllers.TransactionController;
import gens.com.vasinn.controllers.UserController;

/**
 * Created by Gudjon on 6.5.2015.
 */
public class VasiApplication extends Application{

    private TransactionController transController;
    private UserController userController;

    public VasiApplication() {

        super();
        transController = new TransactionController();
        userController = new UserController();
    }

    public TransactionController getTransactionController() {
        return transController;
    }

    public UserController getUserController() {
        return userController;
    }

    public String getLoggedInUsername(){
        SharedPreferences mySharedPreferences;

        mySharedPreferences = getSharedPreferences(getString(R.string.VASINN_PREFERENCE), MODE_PRIVATE);

        // Retrieve the saved values.
        String ret = mySharedPreferences.getString(getString(R.string.VASINN_PREFERENCE_USERNAME), "");

        return ret;
    }

}