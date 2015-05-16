package gens.com.vasinn.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import gens.com.vasinn.R;
import gens.com.vasinn.VasiApplication;
import gens.com.vasinn.activities.MainActivity;
import gens.com.vasinn.activities.TransactionActivity;
import gens.com.vasinn.constants.ActionConstants;
import gens.com.vasinn.controllers.UserController;
import gens.com.vasinn.fragments.CardReaderFragment;
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
    int mAmount;
    VasiApplication vasinn;

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
            mAmount = getArguments().getInt(ARG_PARAM_AMOUNT);
        }

        getDialog().setTitle(mDialogTitle);
        vasinn = (VasiApplication) getActivity().getApplication();
        vasinn.showKeyboard(edtPassword);


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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnConfirmPassword) {
            // Send button was clicked
            String passwordString = edtPassword.getText().toString();
            UserController userController =vasi.getUserController();
            User user = userController.findByName(vasi.getLoggedInUsername());

            if (!passwordString.isEmpty() && user !=null && user.getPassword().equals(passwordString) ) {

                vasinn.hideKeyboard(edtPassword);
                switch (mActionConstants)
                {
                    case ActionConstants.ACTION_MAIN_REFUND:
                        ((TransactionActivity)this.getActivity()).refund();
                        break;
                    case ActionConstants.ACTION_MAIN_CHARGE:
                        CardReaderFragment fragment = CardReaderFragment.newInstance(this.getClass().getName(), mAmount);
                        ((MainActivity) this.getActivity()).FragmentReplace(fragment, "CardReaderFragment");
                        break;
                }
                dismiss();

            } else {
                //Toast.makeText(getActivity(), vasi.getString(R.string.username_and_password_do_not_match), Toast.LENGTH_SHORT).show();

                OkDialog dialog = null;
                dialog = OkDialog.newInstance(getString(R.string.username_and_password_do_not_match_title), getString(R.string.username_and_password_do_not_match));
                dialog.show(getFragmentManager(), "OkDialog");

            }
        } else if (view.getId() == R.id.btnCancelPassword) {
            // Cancel button was clicked
            vasinn.hideKeyboard(edtPassword);
            dismiss();
        }
    }
}