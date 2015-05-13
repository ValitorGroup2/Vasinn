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

            try {
                safetyNumber = Integer.parseInt(edtSaftyNumber.getText().toString());
            }
            catch (Exception e)
            {
               safetyNumber = 0;
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

}
