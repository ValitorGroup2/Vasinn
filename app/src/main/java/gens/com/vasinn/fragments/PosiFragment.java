package gens.com.vasinn.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import gens.com.vasinn.OnSwipeTouchListener;
import gens.com.vasinn.R;
import gens.com.vasinn.activities.MainActivity;
import gens.com.vasinn.constants.ActionConstants;
import gens.com.vasinn.dialogs.UserPasswordDialog;

/**
 * Maid by by Gudjon on 9.5.2015
 */

public class PosiFragment extends Fragment{

    private static final String ARG_PARAM1 = "param1";

    View rootview;
    ImageButton btnCharge;
    private EditText Src;
    private float NumberBf = 0;  //save screen before button press operation
    private String Operation ="";
    private int lastButton = 0;
    private boolean flipForEqual = false;

    public static PosiFragment newInstance(float amount) {

        PosiFragment fragment = new PosiFragment();
        Bundle args = new Bundle();
        args.putFloat(ARG_PARAM1, amount);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.posi_layout, container, false);

        rootview.setOnTouchListener(new OnSwipeTouchListener( this.getActivity().getBaseContext()) {
            @Override
            public void onSwipeLeft() {
                ((MainActivity)getActivity()).onNavigationDrawerItemSelected(1);
            }
        });
        Src = (EditText)rootview.findViewById(R.id.editCalc);
        Src.setEnabled(false);

        btnCharge = (ImageButton) rootview.findViewById(R.id.buttonSaveValue);

        if (getArguments() != null) {
            float num = getArguments().getFloat(ARG_PARAM1);
            if (num == 0 )
                Src.setText("0");
            else
                Src.setText(Integer.toString(Math.round(num)));
        }

        enableChargeButtonIfNumIsValid();


        Src.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enableChargeButtonIfNumIsValid();

            }
        });
//btnCharge
        return rootview;
    }

    public void enableChargeButtonIfNumIsValid()
    {
        float fNum;
        try {
            fNum = Float.parseFloat(Src.getText().toString());
        }
        catch(Exception e)
        {

            fNum = 0; //an error
        }

        btnCharge.setEnabled(fNum != 0);
    }
    public void btnClicked(View v)
    {
        String numb;
        int id = v.getId();
        switch(id)
        {
            case R.id.buttonClear:
                Src.setText("0");
                NumberBf = 0;
                Operation ="";
                break;
            case R.id.buttonPlus:
                mMath("+");
                break;
            case R.id.buttonMinus:
                mMath("-");
                break;
            case R.id.buttonDivide:
                mMath("/");
                break;
            case R.id.buttonMultiply:
                mMath("*");
                break;
            /*case R.id.buttonPercent:
                mMath("%");
                break;
            case R.id.buttonPower:
                mMath("Power");
                break;
            case R.id.buttonSqrt:
                mMath("Sqrt");
                mResult(false);
                break;
            case R.id.buttonSquared:
                mMath("Squared");
                mResult(false);
                break;*/
            case R.id.buttonBackspace:
                numb = Src.getText().toString();
                if (numb.length() < 2) {
                    Src.setText("0");
                }
                else
                {
                    numb = numb.substring(0, numb.length()-1);
                    Src.setText(numb);
                }
                break;
            case R.id.buttonDot:
                numb = Src.getText().toString();
                if (numb.indexOf('.') < 0) {
                    getKeyboard("."); //only one dot allowed
                }

                break;
            case R.id.buttonSign:
                numb = Src.getText().toString();
                Operation = "";
                if (numb.length() < 1) {
                    Src.setText("0");
                }
                else
                {
                    if(numb.charAt(0) == '-')
                        numb = numb.substring(1, numb.length());
                    else
                        numb = "-" + numb;
                    Src.setText(numb);
                }
                break;
            case R.id.buttonEqual:

                mResult(lastButton != R.id.buttonEqual);
                break;
            default:
                numb = ((Button) v).getText().toString();
                getKeyboard(numb);
                break;
        }

        lastButton = id;
        if (lastButton != R.id.buttonEqual) {
            flipForEqual = false;
        }

    }

    public void mMath(String str){
        try {
            NumberBf = Float.parseFloat(Src.getText().toString());
        }
        catch(Exception e)
        {
            Toast.makeText(rootview.getContext(), "Invalid number", Toast.LENGTH_LONG).show();
            NumberBf = 0; //an error
        }

        Operation = str;
        Src.setText("0");
    }

    public void getKeyboard(String str){
        String SrcCurrent = Src.getText().toString();
        if (SrcCurrent.equals("0"))
            SrcCurrent="";
        SrcCurrent += str;
        Src.setText(SrcCurrent);
    }

    public void mResult(boolean saveNumberAf){
        float numAfter;
        float result = 0;
        float numBefore = NumberBf;
        if(Operation.length()<1) {
            return;
        }
        String str;
        try {
            numAfter = Float.parseFloat(Src.getText().toString());
        }
        catch(Exception e){
            Toast.makeText(rootview.getContext(), "Invalid number", Toast.LENGTH_SHORT).show();
            numAfter = 0; //an error
        }


        if (flipForEqual){
            float tmp = numAfter;
            numAfter = numBefore;
            numBefore = tmp;
        }
        if (Operation.equals("Sqrt") || Operation.equals("Squared"))
            str = Operation + " " + String.valueOf(numBefore);
        else
            str = String.valueOf(numBefore) + " " + Operation + " " + String.valueOf(numAfter);

        if (Operation.equals("+"))
        {
            result = numBefore + numAfter;
        }
        else if (Operation.equals("-"))
        {
            result =  numBefore - numAfter;

        }
        else if (Operation.equals("/"))
        {
            if (numAfter == 0) //no null division
                result = 0;
            else
                result = numBefore / numAfter;
        }
        else if (Operation.equals("*"))
        {
            result = numBefore * numAfter;
        }
        else if (Operation.equals("%"))
        {
            result = numBefore * (numAfter /100) ;
        }
        else if (Operation.equals("Power"))
        {
            result = (float)(double)Math.pow(numBefore, numAfter);

        }

        else if (Operation.equals("Sqrt"))
        {
            result = (float)(double)Math.sqrt(numBefore);
            numAfter = numBefore;
        }
        else if (Operation.equals("Squared"))
        {
            result = (float)(double)Math.pow(numBefore, 2);
            numAfter = numBefore;
        }

        if (saveNumberAf){

            NumberBf = numAfter;
            flipForEqual = true;
        }

        Src.setText(String.valueOf(result));
    }

    public void btnSaveValueClick(View view) {
        float num;
        try {
            num = Float.parseFloat(Src.getText().toString());
            float ceilNum = num;
            num = (float) Math.ceil(num);

            if (ceilNum != num) {
                Toast.makeText(rootview.getContext(), "Upphæð var námunduð", Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e){
            Toast.makeText(rootview.getContext(), "Invalid number", Toast.LENGTH_SHORT).show();
            num = 0; //an error
        }

        SharedPreferences sPrefs = this.getActivity().getSharedPreferences(getString(R.string.VASINN_PREFERENCE), Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPrefs.edit();


        editor.putFloat(getString(R.string.VASINN_PREFERENCE_AMOUNT), num);
        if (!editor.commit()) {
            Toast.makeText(rootview.getContext(), "Ekki tókst að skrá töluna " + Float.toString(num), Toast.LENGTH_SHORT).show();
            return;
        }

        if(num < 0) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            UserPasswordDialog dialog = UserPasswordDialog.newInstance(getString(R.string.get_user_password_title), ActionConstants.ACTION_MAIN_CHARGE, num);
            dialog.show(getActivity().getFragmentManager(), "UserPasswordDialog");
        }
        else {

            CardReaderFragment fragment = CardReaderFragment.newInstance(this.getClass().getName(), (int) num);
            ((MainActivity) this.getActivity()).FragmentReplace(fragment, "CardReaderFragment");
        }
    }

}

