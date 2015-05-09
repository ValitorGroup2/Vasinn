package gens.com.vasinn;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        email.requestFocus();
        send.setOnClickListener(this);
        cancel.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnForgotSend) {
            // Send button was clicked
            String emailString = email.getText().toString();
            // TODO: Validate that this email is registered and fetch the relevant password
            // TODO: Make it more secure LOL
            if (!emailString.isEmpty()) {
                try {
                    new SendEmailAsyncTask(emailString).execute();
                    Toast.makeText(getActivity(), getString(R.string.password_sent), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(getActivity(), getString(R.string.password_sent_failure), Toast.LENGTH_SHORT).show();
                }
                dismiss();
            } else {
                Toast.makeText(getActivity(), getString(R.string.forgot_emptyemail), Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.btnForgotCancel) {
            // Cancel button was clicked
            dismiss();
        }
    }
}
