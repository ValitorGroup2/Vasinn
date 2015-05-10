package gens.com.vasinn;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import gens.com.vasinn.controllers.UserController;
import gens.com.vasinn.repos.objects.User;

/**
 * Created by Gudjon on 9.5.2015.
 */
public class UserPasswordDialog extends DialogFragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button btnConfirm, btnCancel;
    EditText edtPassword;
    VasiApplication vasi;
    String mParam1;
    int mTransactionID;
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mTransactionID = getArguments().getInt(ARG_PARAM2);
        }

        getDialog().setTitle(mParam1);

        return view;
    }

    public static UserPasswordDialog newInstance(String strDialogTitle, int transactionID) {
        UserPasswordDialog fragment = new UserPasswordDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, strDialogTitle);
        args.putInt(ARG_PARAM2, transactionID);
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
                ((TransactionActivity)this.getActivity()).refund();
                dismiss();

            } else {
                Toast.makeText(getActivity(), "username and password do NOT match", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.btnCancelPassword) {
            // Cancel button was clicked
            dismiss();
        }
    }


}