package gens.com.vasinn.dialogs;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import gens.com.vasinn.R;

/**
 * Created by Ægir Már Jónsson on 8.5.2015.
 */
public class HelpDialog extends DialogFragment implements View.OnClickListener {
    Button send, cancel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_help, null);
        send = (Button) view.findViewById(R.id.btnHelpConfirm);
        cancel = (Button) view.findViewById(R.id.btnHelpCancel);
        send.setOnClickListener(this);
        cancel.setOnClickListener(this);
        getDialog().setTitle(getString(R.string.help_title));

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnHelpConfirm) {
            // Send button was clicked
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://vasinn.azurewebsites.net/Help/"));
            startActivity(browserIntent);
            dismiss();
        } else if (view.getId() == R.id.btnHelpCancel) {
            // Cancel button was clicked
            dismiss();
        }
    }
}
