package gens.com.vasinn.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
public class OkDialog extends DialogFragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button btnOk;
    TextView tViewMessage;
    String mDialogTitle;
    String mDialogMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ok_dialog, null);
        btnOk = (Button) view.findViewById(R.id.btnOkDlgConfirm);
        btnOk.setOnClickListener(this);
        tViewMessage = (TextView)view.findViewById(R.id.tViewOkDlgMessage);
        if (getArguments() != null) {
            mDialogTitle = getArguments().getString(ARG_PARAM1);
            mDialogMessage = getArguments().getString(ARG_PARAM2);
        }
        else
            mDialogTitle = "Enginn titill settur";

        getDialog().setTitle(mDialogTitle);
        tViewMessage.setText(mDialogMessage);

        return view;
    }

    public static OkDialog newInstance(String dialogTitle, String dialogMessage) {
        OkDialog fragment = new OkDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, dialogTitle);
        args.putString(ARG_PARAM2, dialogMessage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
                dismiss();
    }
}
