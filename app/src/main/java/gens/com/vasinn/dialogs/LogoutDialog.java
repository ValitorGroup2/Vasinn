package gens.com.vasinn.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import gens.com.vasinn.R;
import gens.com.vasinn.activities.LoginActivity;

/**
 * Created by Ægir Már Jónsson on 8.5.2015.
 */
public class LogoutDialog extends DialogFragment implements View.OnClickListener {
    Button accept, cancel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_logout, null);
        accept = (Button) view.findViewById(R.id.btnLogoutConfirm);
        cancel = (Button) view.findViewById(R.id.btnLogoutCancel);
        accept.setOnClickListener(this);
        cancel.setOnClickListener(this);
        getDialog().setTitle(getString(R.string.logout_title));

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnLogoutConfirm) {
            // Send button was clicked
            dismiss();
            logout();
        } else if (view.getId() == R.id.btnLogoutCancel) {
            // Cancel button was clicked
            dismiss();
        }
    }

    private void logout() {
        // select your mode to be either private or public.
        int mode = Activity.MODE_PRIVATE;

        // get the sharedPreference of your context.
        SharedPreferences mySharedPreferences;
        mySharedPreferences = getActivity().getSharedPreferences(getString(R.string.VASINN_PREFERENCE), mode);

        // retrieve an editor to modify the shared preferences
        SharedPreferences.Editor editor = mySharedPreferences.edit();

        // now store your primitive type values. In this case it is true, 1f and Hello! World
        editor.putString(getString(R.string.VASINN_PREFERENCE_USERNAME), "");

        //save the changes that you made
        editor.commit();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
}
