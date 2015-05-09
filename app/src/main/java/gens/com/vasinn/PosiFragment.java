package gens.com.vasinn;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Maid by by Gudjon on 9.5.2015
 */

public class PosiFragment extends Fragment{

    View rootview;

    private EditText Src;
    private float NumberBf = 0;  //save screen before button press operation
    private String Operation ="";
    private int lastButton = 0;
    private boolean flipForEqual = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.posi_layout, container, false);

        Src = (EditText)rootview.findViewById(R.id.editCalc);

        return rootview;
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
        float NumAf;
        float result = 0;
        float NumBf = NumberBf;
        if(Operation.length()<1) {
            return;
        }
        String str;
        try {
            NumAf = Float.parseFloat(Src.getText().toString());
        }
        catch(Exception e){
            Toast.makeText(rootview.getContext(), "Invalid number", Toast.LENGTH_SHORT).show();
            NumAf = 0; //an error
        }


        if (flipForEqual){
            float tmp = NumAf;
            NumAf = NumBf;
            NumBf = tmp;
        }
        if (Operation.equals("Sqrt") || Operation.equals("Squared"))
            str = Operation + " " + String.valueOf(NumBf);
        else
            str = String.valueOf(NumBf) + " " + Operation + " " + String.valueOf(NumAf);

        if (Operation.equals("+"))
        {
            result = NumBf + NumAf ;
        }
        else if (Operation.equals("-"))
        {
            result =  NumBf - NumAf;

        }
        else if (Operation.equals("/"))
        {
            if (NumAf == 0) //no null division
                result = 0;
            else
                result = NumBf / NumAf;
        }
        else if (Operation.equals("*"))
        {
            result = NumBf * NumAf ;
        }
        else if (Operation.equals("%"))
        {
            result = NumBf * (NumAf/100) ;
        }
        else if (Operation.equals("Power"))
        {
            result = (float)(double)Math.pow(NumBf, NumAf);

        }

        else if (Operation.equals("Sqrt"))
        {
            result = (float)(double)Math.sqrt(NumBf);
            NumAf = NumBf;
        }
        else if (Operation.equals("Squared"))
        {
            result = (float)(double)Math.pow(NumBf, 2);
            NumAf = NumBf;
        }

        if (saveNumberAf){

            NumberBf = NumAf;
            flipForEqual = true;
        }

        Src.setText(String.valueOf(result));
    }

    public void btnSaveValueClick(View view) {
        float num;
        try {
            num = Float.parseFloat(Src.getText().toString());

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
        CardReaderFragment fragment = CardReaderFragment.newInstance(this.getClass().getName(), num);
        ((MainActivity) this.getActivity()).FragmentReplace(fragment);

    }

}

