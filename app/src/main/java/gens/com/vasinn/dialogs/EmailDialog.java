package gens.com.vasinn.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import gens.com.vasinn.R;
import gens.com.vasinn.SendEmailAsyncTask;
import gens.com.vasinn.VasiApplication;
import gens.com.vasinn.constants.ActionConstants;
import gens.com.vasinn.controllers.UserController;
import gens.com.vasinn.repos.objects.Transaction;
import gens.com.vasinn.repos.objects.User;

/**
 * Created by Ægir Már Jónsson on 8.5.2015.
 */
public class EmailDialog extends DialogFragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    Button send, cancel;
    EditText email;
    VasiApplication vasinn;
    String mDialogTitle;
    int mAction;
    int mTransactionID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_email, null);
        send = (Button) view.findViewById(R.id.btnForgotSend);
        cancel = (Button) view.findViewById(R.id.btnForgotCancel);
        email = (EditText) view.findViewById(R.id.edtTextForgot);
        send.setOnClickListener(this);
        cancel.setOnClickListener(this);

        vasinn = (VasiApplication) getActivity().getApplication();
        vasinn.showKeyboard(email);
        mAction = ActionConstants.ACTION_ASK_USER_EMAIL;
        mTransactionID = -1;
        if (getArguments() != null) {
            mDialogTitle = getArguments().getString(ARG_PARAM1);
            mAction = getArguments().getInt(ARG_PARAM2);
            mTransactionID = getArguments().getInt(ARG_PARAM3);
        }
        else
            mDialogTitle = getString(R.string.forgot_password_title);

          getDialog().setTitle(mDialogTitle);



        return view;
    }

    public static EmailDialog newInstance(String dialogTitle, int action, int transactionID) {
        EmailDialog fragment = new EmailDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, dialogTitle);
        args.putInt(ARG_PARAM2, action);
        args.putInt(ARG_PARAM3, transactionID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnForgotSend) {
            // Send button was clicked
            String emailString = email.getText().toString();
            if (emailString.isEmpty()) {
                Toast.makeText(getActivity(), getString(R.string.forgot_emptyemail), Toast.LENGTH_LONG).show();
                return;
            }

            VasiApplication vasi = ((VasiApplication) this.getActivity().getApplication());
            UserController userController = vasi.getUserController();
            if (mAction == ActionConstants.ACTION_ASK_CUSTOMER_EMAIL){
                Transaction transaction = vasi.getTransactionController().findById(mTransactionID);


                //todo: simplify repeated code

                try {
                    new SendEmailAsyncTask(emailString,
                            vasi.getString(R.string.email_user_receipt_subject),
                            transaction.toString(false)
                    ).execute();

                    Toast.makeText(getActivity(), vasi.getString(R.string.email_receipt_sent_ok), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(getActivity(), vasi.getString(R.string.email_receipt_sent_fail), Toast.LENGTH_LONG).show();
                }
                vasinn.hideKeyboard(email);
                dismiss();










            }
            else
            {
                User user = userController.findByEmail(emailString);
                if (user != null && user.getEmail().equals(emailString)) {
                    try {
                        new SendEmailAsyncTask(emailString,
                                getString(R.string.email_forgot_password_subject),
                                getString(R.string.email_forgot_password_body_before) + user.getPassword()
                                        + getString(R.string.email_forgot_password_body_after)
                        ).execute();

                        Toast.makeText(getActivity(), getString(R.string.password_sent), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.getMessage();
                        Toast.makeText(getActivity(), getString(R.string.password_sent_failure), Toast.LENGTH_LONG).show();
                    }
                    vasinn.hideKeyboard(email);
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), vasi.getString(R.string.email_not_registered), Toast.LENGTH_LONG).show();
                }
        }
        } else if (view.getId() == R.id.btnForgotCancel) {
            // Cancel button was clicked
            vasinn.hideKeyboard(email);
            dismiss();
        }
    }
}
