package gens.com.vasinn.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import gens.com.vasinn.R;
import gens.com.vasinn.VasiApplication;
import gens.com.vasinn.constants.ActionConstants;
import gens.com.vasinn.controllers.TransactionController;
import gens.com.vasinn.controllers.UserController;
import gens.com.vasinn.dialogs.EmailDialog;
import gens.com.vasinn.dialogs.HelpDialog;
import gens.com.vasinn.dialogs.ReportDialog;
import gens.com.vasinn.dialogs.UserPasswordDialog;
import gens.com.vasinn.repos.objects.Transaction;


public class TransactionActivity extends ActionBarActivity {


    Transaction transaction = null;
    TransactionController transactionController;
    UserController userController;
    VasiApplication vasi;
    Menu menu;
    private View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        vasi = ((VasiApplication) this.getApplication());
        transactionController = vasi.getTransactionController();
        userController = vasi.getUserController();
        Bundle bundle = getIntent().getExtras();
        int i = -1;
        if (bundle != null) {
            i = bundle.getInt(getString(R.string.TRANSACTION_KEY_ID));
        }

        transaction = transactionController.findById(i);

        loadTransaction(transaction);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, Menu.NONE, vasi.getLoggedInUsername()).setEnabled(false).setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT + MenuItem.SHOW_AS_ACTION_ALWAYS);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_transaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id)
        {
            /*case R.id.action_logout:
                logoutConfirm();
                return true;*/
            case android.R.id.home:
                // app icon in action bar clicked;
                this.onBackPressed();
                return true;
            case R.id.action_help:
                FragmentManager manager = getFragmentManager();
                HelpDialog helpDialog = new HelpDialog();
                helpDialog.show(manager, "HelpDialog");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void charge() {
        if (transaction.getAmount() != 0 ){
            Transaction oldTransaction = transaction;
            transaction = transactionController.add(transaction.getAmount(),
                    ((VasiApplication) this.getApplication()).getLoggedInUsername(),
                    transaction.getCard(), transaction.getAmount() > 0);

            loadTransaction(transaction);
        }

    }
    public void refund()
    {
        if (transaction.getAmount() != 0 ){
            Transaction oldTransaction = transaction;
            transaction = transactionController.add(transaction.getAmount()* -1,
            ((VasiApplication) this.getApplication()).getLoggedInUsername(),
            transaction.getCard(), false);
            oldTransaction.setIsRefundable(false);
            transactionController.updateTransaction(oldTransaction); //make old transaction un-refundable to prevent double booking

            loadTransaction(transaction);
        }
    }
    public void onRefundClick(View view) {

        UserPasswordDialog dialog = UserPasswordDialog.newInstance(getString(R.string.get_user_password_title), ActionConstants.ACTION_MAIN_REFUND, (float) transaction.getAmount());
        dialog.show(getFragmentManager(), "UserPasswordDialog");

    }


    public void onSendReceiptClick(View view) {
        /*FragmentManager manager = getFragmentManager();
        EmailDialog emailDialog = new EmailDialog();
        emailDialog.show(manager, "EmailDialog");*/

        EmailDialog dialog = EmailDialog.newInstance(getString(R.string.ask_user_email_dialog_title), ActionConstants.ACTION_ASK_CUSTOMER_EMAIL, transaction.getId());
        dialog.show(getFragmentManager(), "UserPasswordDialog");
    }

    @Override
    public void onBackPressed() {

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String callingClassName =  bundle.getString(getString(R.string.VASINN_CALLING_CLASS));

            this.getIntent().putExtra(getString(R.string.VASINN_CALLING_CLASS), "");
            if ( callingClassName == null
                 || callingClassName.equals("gens.com.vasinn.activities.MainActivity")
                 || callingClassName.equals("")){


                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return;
            }
            else if (callingClassName != null && callingClassName.equals("gens.com.vasinn.fragments.SalesFragment$1")){
                super.onBackPressed();
            }
        }

       //ignoring back button
    }

    public void onBackClick(View view) {
        this.onBackPressed();
    }

    public void loadTransaction(Transaction newTransaction) {

        transaction = newTransaction;

        if (transaction != null)
        {
            ((TextView)findViewById(R.id.tViewTransactionID)).setText(transaction.getIdString());
            ((TextView)findViewById(R.id.tViewTransactionTime)).setText(transaction.getDateTimeString());
            ((TextView)findViewById(R.id.tViewTransactionCard)).setText(transaction.getCard());
            ((TextView)findViewById(R.id.tViewTransactionAmount)).setText(transaction.getAmountString());
            ((TextView)findViewById(R.id.tViewTransactionUsername)).setText(transaction.getUserName());

            ((Button)findViewById(R.id.btnRefund)).setEnabled(transaction.isRefundable());


        }
        else{
            ((TextView)findViewById(R.id.tViewTransactionID)).setText("");
            ((TextView)findViewById(R.id.tViewTransactionTime)).setText("");
            ((TextView)findViewById(R.id.tViewTransactionCard)).setText("");
            ((TextView)findViewById(R.id.tViewTransactionAmount)).setText("");
            ((TextView)findViewById(R.id.tViewTransactionUsername)).setText("Færsla fannst ekki");
        }
    }

    public void onButtonCardReaderCancel(View view) {
        this.onBackPressed();
    }

    public void onSendReportClick(View view) {
        ReportDialog dialog = ReportDialog.newInstance(getString(R.string.report_dialog_title), transaction.getId());
        dialog.show(getFragmentManager(), "ReportDialog");

    }

    public void logoutConfirm() {
        // Alert to check if user is sure about logging out
        AlertDialog.Builder logoutAlert = new AlertDialog.Builder(this);
        logoutAlert.setMessage(getString(R.string.lc_question))
                .setTitle(getString(R.string.lc_title))
                .setIcon(R.drawable.scan_card)
                .setPositiveButton(getString(R.string.lc_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        logout();
                    }
                })
                .setNegativeButton(getString(R.string.lc_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        logoutAlert.show();
    }

    private void logout() {
        // select your mode to be either private or public.
        int mode = Activity.MODE_PRIVATE;

        // get the sharedPreference of your context.
        SharedPreferences mySharedPreferences;
        mySharedPreferences = getSharedPreferences(getString(R.string.VASINN_PREFERENCE), mode);

        // retrieve an editor to modify the shared preferences
        SharedPreferences.Editor editor = mySharedPreferences.edit();

        // now store your primitive type values. In this case it is true, 1f and Hello! World
        editor.putString(getString(R.string.VASINN_PREFERENCE_USERNAME), "");

        //save the changes that you made
        editor.commit();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
