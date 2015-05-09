package gens.com.vasinn;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import gens.com.vasinn.controllers.TransactionController;
import gens.com.vasinn.controllers.UserController;
import gens.com.vasinn.repos.objects.Transaction;


public class TransactionActivity extends ActionBarActivity {


    Transaction transaction = null;
    TransactionController transactionController;
    UserController userController;
    private View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        VasiApplication vasi = ((VasiApplication) this.getApplication());
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onRefundClick(View view) {
        Toast.makeText(getApplicationContext(), "onRefundClick", Toast.LENGTH_SHORT).show();

        if (transaction.getAmount() > 0 ){

            transaction = transactionController.add(transaction.getAmount()* -1,
                    ((VasiApplication) this.getApplication()).getLoggedInUsername(),
                    transaction.getCard());
            loadTransaction(transaction);
        }
    }

    public void onSendReportClick(View view) {
        Toast.makeText(getApplicationContext(), "onSendReportClick", Toast.LENGTH_SHORT).show();
    }

    public void onSendReceiptClick(View view) {
        Toast.makeText(this.getBaseContext(), "onSendReceiptClick", Toast.LENGTH_SHORT).show();
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

            if(transaction.getAmount() < 0)
            {
                ((Button)findViewById(R.id.btnRefund)).setEnabled(false);
            }

        }
        else{
            ((TextView)findViewById(R.id.tViewTransactionID)).setText("");
            ((TextView)findViewById(R.id.tViewTransactionTime)).setText("");
            ((TextView)findViewById(R.id.tViewTransactionCard)).setText("");
            ((TextView)findViewById(R.id.tViewTransactionAmount)).setText("");
            ((TextView)findViewById(R.id.tViewTransactionUsername)).setText("Færsla fannst ekki");
        }
    }
}