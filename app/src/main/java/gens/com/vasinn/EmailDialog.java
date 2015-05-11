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
 * Created by Ægir Már Jónsson on 8.5.2015.
 */
public class EmailDialog extends DialogFragment implements View.OnClickListener {
    Button send, cancel;
    EditText email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.email_dialog, null);
        send = (Button) view.findViewById(R.id.btnForgotSend);
        cancel = (Button) view.findViewById(R.id.btnForgotCancel);
        email = (EditText) view.findViewById(R.id.edtTextForgot);
        send.setOnClickListener(this);
        cancel.setOnClickListener(this);

        email.requestFocus();
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnForgotSend) {
            // Send button was clicked
            String emailString = email.getText().toString();
            if (emailString.isEmpty())
            {
                Toast.makeText(getActivity(), getString(R.string.forgot_emptyemail), Toast.LENGTH_LONG).show();
                return;
            }

            VasiApplication vasi = ((VasiApplication) this.getActivity().getApplication());
            UserController userController =vasi.getUserController();
            User user = userController.findByEmail(emailString);
            if (user !=null && user.getEmail().equals(emailString) ) {
                try {
                    new SendEmailAsyncTask(emailString,
                            getString(R.string.email_forgot_password_subject),
                            getString(R.string.email_forgot_password_body_before)  + user.getPassword()
                                    + getString(R.string.email_forgot_password_body_after)
                    ).execute();

                    Toast.makeText(getActivity(), getString(R.string.password_sent), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(getActivity(), getString(R.string.password_sent_failure), Toast.LENGTH_LONG).show();
                }
                dismiss();
            } else {
                Toast.makeText(getActivity(), vasi.getString(R.string.email_not_registered), Toast.LENGTH_LONG).show();
            }
        } else if (view.getId() == R.id.btnForgotCancel) {
            // Cancel button was clicked
            dismiss();
        }
    }
}
