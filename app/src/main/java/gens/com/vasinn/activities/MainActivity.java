package gens.com.vasinn.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.emvnfccard.provider.Provider;
import com.github.devnied.emvnfccard.fragment.IRefreshable;
import com.github.devnied.emvnfccard.model.EmvCard;
import com.github.devnied.emvnfccard.parser.EmvParser;
import com.github.devnied.emvnfccard.utils.AtrUtils;
import com.github.devnied.emvnfccard.utils.NFCUtils;
import com.github.devnied.emvnfccard.utils.SimpleAsyncTask;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import fr.devnied.bitlib.BytesUtils;
import gens.com.vasinn.R;
import gens.com.vasinn.VasiApplication;
import gens.com.vasinn.dialogs.CardNumberDialog;
import gens.com.vasinn.dialogs.LogoutDialog;
import gens.com.vasinn.fragments.NavigationDrawerFragment;
import gens.com.vasinn.fragments.PosiFragment;
import gens.com.vasinn.fragments.SalesFragment;
import gens.com.vasinn.repos.objects.Transaction;

//com/github/devnied/emvnfccard/utils/SimpleAsyncTask.java


public class MainActivity extends /*FragmentActivity*/ ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    Fragment objFragment = null;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
        /*Implementing copy paste*/

    private NFCUtils mNfcUtils;
    private Provider mProvider = new Provider();

    private EmvCard mReadCard;

    private WeakReference<IRefreshable> mRefreshableContent;
    private byte[] lastAts;
    private ProgressDialog mDialog;
    private AlertDialog mAlertDialog;
    private Menu menu;

    VasiApplication vasi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        vasi = (VasiApplication) this.getApplication();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // init NfcUtils
        mNfcUtils = new NFCUtils(this);
        vasi.setChargedAmount(0);

        if (getIntent().getAction() == NfcAdapter.ACTION_TECH_DISCOVERED) {
            onNewIntent(getIntent());
        }

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        String fragmentTag ="";
        switch (position) {
            case 0:
                double amount = vasi.getChargedAmount();
                vasi.setChargedAmount(0);
                objFragment = (Fragment)new PosiFragment().newInstance((float)amount);
                fragmentTag = "PosiFragment";
                break;
            case 1:
                objFragment = new SalesFragment();
                fragmentTag = "SalesFragment";
                break;
            case 2:
                logoutConfirm();
                return;
        }

        FragmentReplace(objFragment, fragmentTag);


    }
    // update the main content by replacing fragments
    public void FragmentReplace(Fragment objFragment, String fragmentTag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, objFragment, fragmentTag)
                .commit();

    }

    public void logoutConfirm() {
        // Alert to check if user is sure about logging out
        android.app.FragmentManager manager = getFragmentManager();
        LogoutDialog logoutDialog = new LogoutDialog();
        logoutDialog.show(manager, "LogoutDialog");
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

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, Menu.NONE, vasi.getLoggedInUsername()).setEnabled(false).setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT + MenuItem.SHOW_AS_ACTION_ALWAYS);
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }

        //this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    public void onButtonCardReaderGetCardNumber(View view) {


        android.app.FragmentManager manager = getFragmentManager();
        CardNumberDialog dialog = new CardNumberDialog();
        dialog.show(manager, "CardNumberDialog");

    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_main, container, false);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

    }

    @Override
    protected void onResume() {
        mNfcUtils.enableDispatch();

        // Check NFC enable
        if (!NFCUtils.isNfcEnable(getApplicationContext())) {
            backToHomeScreen();

            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            alertbox.setTitle(getString(R.string.msg_info));
            alertbox.setMessage(getString(R.string.msg_nfc_disable));
            alertbox.setPositiveButton(getString(R.string.msg_activate_nfc), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    Intent intent;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                    } else {
                        intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    }
                    dialog.dismiss();
                    startActivity(intent);
                }
            });
            alertbox.setCancelable(false);
            mAlertDialog = alertbox.show();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNfcUtils.disableDispatch();
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);


        final Tag mTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (mTag != null) {

            new SimpleAsyncTask() {
                /**
                 * Tag comm
                 */
                private IsoDep mTagcomm;
                /**
                 * Emv Card
                 */
                private EmvCard mCard;

                /**
                 * Boolean to indicate exception
                 */
                private boolean mException;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    backToHomeScreen();
                    mProvider.getLog().setLength(0);
                    // Show dialog
                    if (mDialog == null) {
                        mDialog = ProgressDialog.show(MainActivity.this, getString(R.string.card_reading_title),
                                getString(R.string.card_reading_desc), true, false);
                    } else {
                        mDialog.show();
                    }
                }

                @Override
                protected void doInBackground() {

                    mTagcomm = IsoDep.get(mTag);
                    if (mTagcomm == null) {

                        Toast.makeText(getApplicationContext(), getString(R.string.error_communication_nfc), Toast.LENGTH_LONG).show();
                        return;
                    }
                    mException = false;

                    try {
                        if (vasi.getChargedAmount() == 0)
                            return;
                        mReadCard = null;
                        // Open connection
                        mTagcomm.connect();
                        lastAts = getAts(mTagcomm);

                        mProvider.setmTagCom(mTagcomm);

                        EmvParser parser = new EmvParser(mProvider, true);
                        mCard = parser.readEmvCard();
                        if (mCard != null) {
                            mCard.setAtrDescription(extractAtsDescription(lastAts));
                        }
                    } catch (IOException e) {
                        mException = true;
                    } finally {
                        // close tagcomm
                        IOUtils.closeQuietly(mTagcomm);
                    }
                }

                @Override
                protected void onPostExecute(final Object result) {
                    // close dialog
                    if (mDialog != null) {
                        mDialog.cancel();
                    }

                    String strMessage = "";

                    if (!mException) {
                        if (vasi.getChargedAmount() == 0 )
                            return;

                        if (mCard != null) {
                            if (StringUtils.isNotBlank(mCard.getCardNumber())) {
                                strMessage += getText(R.string.card_read);
                                strMessage += "\nCard number:" + mCard.getCardNumber();
                                strMessage += "\nCard Aid:" + mCard.getAid();
                                strMessage += "\nProvider:" + mCard.getType();
                                if (mCard.getExpireDate() != null) {
                                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                                    strMessage += "\nExpires: " + df.format(mCard.getExpireDate());
                                }
                                strMessage += "\nPin tries left: " + mCard.getLeftPinTry();

                                mReadCard = mCard;
                            } else if (mCard.isNfcLocked()) {
                                strMessage += getText(R.string.nfc_locked);
                            }
                        } else {
                            strMessage += getText(R.string.error_card_unknown);

                        }
                    } else {
                        strMessage += getText(R.string.error_communication_nfc);
                    }


                    Date now = new Date();
                    if ((mCard != null)
                            && (StringUtils.isNotBlank(mCard.getCardNumber()))
                            ) {

                        if (now.before(mCard.getExpireDate())) {
                            //Card is not expired

                            double amount = vasi.getChargedAmount();
                            vasi.setChargedAmount(0);


                            doTransaction(amount, mCard.getCardNumber(), 0, mCard.getExpireDate().getYear(), mCard.getExpireDate().getMonth());

                            return;
                        }
                        else
                        {
                            strMessage = getString(R.string.card_is_expired);
                        }

                    }
                        //unable to read card.
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Lestur korts");
                        builder.setMessage(strMessage);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing but close the dialog
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();

                    refreshContent();
                }
            }.execute();
        }
    }
//expiresMonth 0 = january
    public boolean doTransaction(double amount, String strCardNumber, int sequrityNumber, int expiresYear, int expiresMonth){
        //todo: shall we do anything with the card sequrityNumber,  expiresYear and expiresMonth
        boolean isRefundable = (amount > 0);

        Transaction item = vasi.getTransactionController().add(strCardNumber, amount, vasi.getLoggedInUsername(), isRefundable);
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.TRANSACTION_KEY_ID), item.getId());
        String str = this.getClass().getName();
        bundle.putString(getString(R.string.VASINN_CALLING_CLASS), str);
        Intent intent = new Intent(MainActivity.this, TransactionActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        return true;  //todo: do some error checking
    }
    public void backToHomeScreen() {

    }


    /**
     * Get ATS from isoDep
     *
     * @param pIso
     *            isodep
     * @return ATS byte array
     */
    private byte[] getAts(final IsoDep pIso) {
        byte[] ret = null;
        if (pIso.isConnected()) {
            // Extract ATS from NFC-A
            ret = pIso.getHistoricalBytes();
            if (ret == null) {
                // Extract ATS from NFC-B
                ret = pIso.getHiLayerResponse();
            }
        }
        return ret;
    }

    /**
     * Method used to get description from ATS
     *
     * @param pAts
     *            ATS byte
     */
    public Collection<String> extractAtsDescription(final byte[] pAts) {
        return AtrUtils.getDescriptionFromAts(BytesUtils.bytesToString(pAts));
    }
    private void refreshContent() {
        if (mRefreshableContent != null && mRefreshableContent.get() != null) {
            mRefreshableContent.get().update();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    /**
     * Method used to clear data
     */
    public void clear() {
        mReadCard = null;
        mProvider.getLog().setLength(0);
        IRefreshable content = mRefreshableContent.get();
        if (content != null) {
            content.update();
        }
    }
    public byte[] getLastAts() {
        return lastAts;
    }

    @Override
    public void onBackPressed() {

       Fragment f;
        String str = objFragment.getClass().getName();
        f = (Fragment)getSupportFragmentManager().findFragmentByTag("PosiFragment");

        if(f != null && f.isVisible()) {
            moveTaskToBack(true);
            finish();  //not sure that this is needed
            return;
        }
        
        super.onBackPressed();
    }

    public void btnClicked(View v){
        ((PosiFragment)objFragment).btnClicked(v);

    }
    public void btnSaveValueClick(View v) {
        ((PosiFragment) objFragment).btnSaveValueClick(v);

    }

    public void onButtonCardReaderCancel(View view) {

        this.onBackPressed();
    }

}
