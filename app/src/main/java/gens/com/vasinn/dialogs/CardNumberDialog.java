package gens.com.vasinn.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.Date;

import gens.com.vasinn.R;
import gens.com.vasinn.VasiApplication;
import gens.com.vasinn.activities.MainActivity;
import gens.com.vasinn.constants.CardConstants;


/**
 * Created by Gudjon on 13.5.2015.
 */

public class CardNumberDialog extends DialogFragment implements OnClickListener {


    private EditText edtCardNumber;
    private EditText edtSaftyNumber;
    private Button btnCharge;
    VasiApplication vasi;
    public CardNumberDialog() {
        // Required empty public constructor
    }

    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnChargeCardGetNumber)
        {

            Spinner spinner = (Spinner)getView().findViewById(R.id.spinnerMonth);
            int month, year, safetyNumber;
            month = spinner.getSelectedItemPosition();  //month 0 is januar
            spinner = (Spinner)getView().findViewById(R.id.spinnerYear);
            year = 2015 + spinner.getSelectedItemPosition();//first year in the list is 2015
            safetyNumber = getSafetyNumber();

            if (hasCardExpired(year, month)){

                OkDialog dialog = new OkDialog().newInstance(getString(R.string.expiration_date_title), getString(R.string.card_is_expired));
                dialog.show(getFragmentManager(), "OkDialog");
                return;
            }
            else if (safetyNumber < 0 ) {
                OkDialog dialog = new OkDialog().newInstance(getString(R.string.safety_number_title), getString(R.string.safety_number_wrong_format));
                dialog.show(getFragmentManager(), "OkDialog");
                return;

            }
            
            double amount = vasi.getChargedAmount();
            vasi.setChargedAmount(0);
            String strNum = edtCardNumber.getText().toString();
            String strType = vasi.cardTypeToString(vasi.getCardType(strNum));
            ((MainActivity)getActivity()).doTransaction(strType, amount, strNum, safetyNumber, year, month);
        }
        this.dismiss();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_card_number_dialog, null);
        vasi = (VasiApplication) getActivity().getApplication();
        ((Button) view.findViewById(R.id.btnCancelCardGetNumber)).setOnClickListener(this);
        btnCharge = (Button)view.findViewById(R.id.btnChargeCardGetNumber);

        btnCharge.setEnabled(false);
        btnCharge.setOnClickListener(this);
        edtCardNumber = (EditText)view.findViewById(R.id.edtCardNumber);
        edtSaftyNumber = (EditText)view.findViewById(R.id.edtSafetyNumber);

        getDialog().setTitle(getString(R.string.skra_kortanumer_title));

        edtCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                btnCharge.setEnabled(isValidCardNumber(edtCardNumber.getText().toString()));
            }
        });

        return view;
    }


    public boolean isValidCardNumber(String number){


        return vasi.getCardType(number) != CardConstants.UNKNOWN;
    }

    //year, the expiration year
    //month the expiration month, januar is number 0, feb number 1.
    public  boolean hasCardExpired(int year, int month){

        Calendar cal = Calendar.getInstance();
        int nowMonth = cal.get(Calendar.MONTH);
        int nowYear = cal.get(Calendar.YEAR);

        boolean bRet;
        if (nowYear != year) {
            bRet = (nowYear > year);
        }
        else
        {
            bRet = nowMonth > month;
        }
        return bRet;
    }
    //returns the safety number
    //return less than zero if number is invalid
    public int getSafetyNumber(){

        String strSaftyNumber = edtSaftyNumber.getText().toString();

        if (strSaftyNumber.isEmpty())
            return 0;
        
        int safetyNumber;
        
        try {
            safetyNumber = Integer.parseInt(strSaftyNumber);
        }
        catch (Exception e)
        {
            safetyNumber = -1;
        }
        
        if (safetyNumber < 0 || safetyNumber > 9999)
            return -1;

        if (    (  safetyNumber > 99 && safetyNumber < 1000 )
             || ( safetyNumber > 999 && safetyNumber < 10000 ))
            return safetyNumber; //apparently valid is 3 digits or 4 digits

        return -1;
    }

}
