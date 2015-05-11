package gens.com.vasinn;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import gens.com.vasinn.controllers.UserController;
import gens.com.vasinn.repos.objects.User;

/**
 * Created by Gudjon on 9.5.2015.
 */
public class UserPasswordDialog extends DialogFragment implements View.OnClickListener {
    private static final String ARG_PARAM_TITLE = "param1";
    private static final String ARG_PARAM_ACTION = "param2";
    private static final String ARG_PARAM_AMOUNT = "param3";


    Button btnConfirm, btnCancel;
    EditText edtPassword;
    VasiApplication vasi;
    String mDialogTitle;
    int mActionConstants;
    float mAmount;

    @Override
    public void show(android.app.FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_user_password, null);
        btnConfirm = (Button) view.findViewById(R.id.btnConfirmPassword);
        btnCancel = (Button) view.findViewById(R.id.btnCancelPassword);
        edtPassword = (EditText) view.findViewById(R.id.edtTextPassword);
        btnConfirm.requestFocus();
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        vasi = ((VasiApplication) this.getActivity().getApplication());

        if (getArguments() != null) {
            mDialogTitle = getArguments().getString(ARG_PARAM_TITLE);
            mActionConstants = getArguments().getInt(ARG_PARAM_ACTION);
            mAmount = getArguments().getFloat(ARG_PARAM_AMOUNT);
        }

        getDialog().setTitle(mDialogTitle);
        showKeyboard();


        return view;
    }

    public static UserPasswordDialog newInstance(String strDialogTitle, int inActionConstants, float amount) {
        UserPasswordDialog fragment = new UserPasswordDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_TITLE, strDialogTitle);
        args.putInt(ARG_PARAM_ACTION, inActionConstants);
        args.putFloat(ARG_PARAM_AMOUNT, amount);
        fragment.setArguments(args);
        return fragment;
    }
    public void showKeyboard(){
        edtPassword.requestFocus();
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
    public void hideKeyboard(){
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.hideSoftInputFromWindow(edtPassword.getWindowToken(), 0);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnConfirmPassword) {
            // Send button was clicked
            String passwordString = edtPassword.getText().toString();
            UserController userController =vasi.getUserController();
            User user = userController.findByName(vasi.getLoggedInUsername());

            hideKeyboard();


            if (!passwordString.isEmpty() && user !=null && user.getPassword().equals(passwordString) ) {

                switch (mActionConstants)
                {
                    case ActionConstants.ACTION_MAIN_REFUND:
                        ((TransactionActivity)this.getActivity()).refund();
                    case ActionConstants.ACTION_MAIN_CHARGE:
                        CardReaderFragment fragment = CardReaderFragment.newInstance(this.getClass().getName(), mAmount);
                        ((MainActivity) this.getActivity()).FragmentReplace(fragment);

                    break;
                }

                dismiss();

            } else {
                Toast.makeText(getActivity(), vasi.getString(R.string.username_and_password_do_not_match), Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.btnCancelPassword) {
            // Cancel button was clicked
            hideKeyboard();
            dismiss();
        }
    }


}