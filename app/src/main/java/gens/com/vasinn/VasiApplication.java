package gens.com.vasinn;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.regex.Pattern;

import gens.com.vasinn.constants.CardConstants;
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
    public float getChargedAmount(){
        SharedPreferences mySharedPreferences;

        mySharedPreferences = getSharedPreferences(getString(R.string.VASINN_PREFERENCE), MODE_PRIVATE);

        // Retrieve the saved values.
        float ret = mySharedPreferences.getFloat(getString(R.string.VASINN_PREFERENCE_AMOUNT), 0);


        return ret;
    }
    public void setChargedAmount(float amount){
        SharedPreferences sPrefs;

        sPrefs = getSharedPreferences(getString(R.string.VASINN_PREFERENCE), MODE_PRIVATE);

        SharedPreferences.Editor editor = sPrefs.edit();
        editor.putFloat(getString(R.string.VASINN_PREFERENCE_AMOUNT), amount);
        editor.commit();

    }

    public void showKeyboard(EditText edtText){
        edtText.requestFocus();
        InputMethodManager imgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void hideKeyboard(EditText edtText){
        InputMethodManager imgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.hideSoftInputFromWindow(edtText.getWindowToken(), 0);
    }


}