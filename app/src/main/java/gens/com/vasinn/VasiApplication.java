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

    public int getCardType(String number){

        if (number == null || number.length() < 15)
            return CardConstants.UNKNOWN;

        String MAESTRO = "(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\\d{8}|(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\\d{9}|(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\\d{10}|(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\\d{11}|(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\\d{12}|(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\\d{13}|(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\\d{14}|(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\\d{15}$";
        String MASTERCARD = "^(?!.*(?:(?:5018|5020|5038\\d{12})))[5][0-5].{14}$";
        String SOLO = "(6334|6767)\\d{12}|(6334|6767)\\d{14}|(6334|6767)\\d{15}";
        String SWITCH = "(4903|4905|4911|4936|6333|6759)\\d{12}|(4903|4905|4911|4936|6333|6759)\\d{14}|(4903|4905|4911|4936|6333|6759)\\d{15}|(564182|633110)\\d{10}|(564182|633110)\\d{12}|(564182|633110)\\d{13}$";
        String VISA = "^(?!.*(?:(?:4026|4405|4508|4844|4913|4917)\\d{12}|417500\\d{10}))4\\d{15}$";
        String VISA_ELECTRON = "(4026|4405|4508|4844|4913|4917)\\d{12}|417500\\d{10}$";


        if (Pattern.compile(MAESTRO).matcher(number).matches())
            return CardConstants.MAESTRO;
        if (Pattern.compile(MASTERCARD).matcher(number).matches())
            return CardConstants.MASTERCARD;
        if (Pattern.compile(SOLO).matcher(number).matches())
            return CardConstants.SOLO;
        if (Pattern.compile(SWITCH).matcher(number).matches())
            return CardConstants.SWITCH;
        if (Pattern.compile(VISA_ELECTRON).matcher(number).matches())
            return CardConstants.VISA_ELECTRON;
        if (Pattern.compile(VISA).matcher(number).matches())
            return CardConstants.VISA;

        return CardConstants.UNKNOWN;

    }
    public String cardTypeToString(int cardType){


        switch(cardType) {
            case CardConstants.MAESTRO:
                return "Maestro";
            case CardConstants.MASTERCARD:
                return "Mastercard";
            case CardConstants.SOLO:
                return "Solo";
            case CardConstants.SWITCH:
                return "Switch";
            case CardConstants.VISA_ELECTRON:
                return "Visa electron";
            case CardConstants.VISA:
                return "Visa";
        }
        return null;
    }
}