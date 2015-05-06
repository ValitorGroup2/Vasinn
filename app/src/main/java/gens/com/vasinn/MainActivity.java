package gens.com.vasinn;

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
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.List;

import fr.devnied.bitlib.BytesUtils;
import gens.com.vasinn.controllers.TransactionController;
import gens.com.vasinn.controllers.UserController;
import gens.com.vasinn.repos.objects.Transaction;

//com/github/devnied/emvnfccard/utils/SimpleAsyncTask.java


public class MainActivity extends /*FragmentActivity*/ ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VasiApplication vasi = ((VasiApplication) this.getApplication());
        TransactionController transCon = vasi.getTransactionController();
        UserController userCon = vasi.getUserController();

        List<Transaction> range = transCon.getRange(0, 3);
        String stuff = "";
        for(int i = 0; i < range.size(); i++)
        {
            stuff += "\nid:"      + range.get(i).getId();
            stuff += "\nTími:"    + range.get(i).getDateTimeString();
            stuff += "\nUpphæð:"  + range.get(i).getAmount();
            stuff += "\nNotandi:" + range.get(i).getUserName();
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // init NfcUtils
        mNfcUtils = new NFCUtils(this);



        if (getIntent().getAction() == NfcAdapter.ACTION_TECH_DISCOVERED) {
            onNewIntent(getIntent());
        }

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void logout() {
        // select your mode to be either private or public.
        int mode = Activity.MODE_PRIVATE;

        // get the sharedPreference of your context.
        SharedPreferences mySharedPreferences;
        mySharedPreferences = getSharedPreferences("vasinnPreference", mode);

        // retrieve an editor to modify the shared preferences
        SharedPreferences.Editor editor = mySharedPreferences.edit();

        // now store your primitive type values. In this case it is true, 1f and Hello! World
        editor.putString("myUsername", "");

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
            case 3:
                // Logout the user
                logout();
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
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id)
        {
            case R.id.action_logout:
                logout();
                return true;
        }

        return super.onOptionsItemSelected(item);
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
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
                    Intent intent = null;
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
                        mDialog = ProgressDialog.show(MainActivity.this, getString(R.string.card_reading),
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
                    String temp = "";
                    if (!mException) {
                        if (mCard != null) {
                            if (StringUtils.isNotBlank(mCard.getCardNumber())) {
                                temp += getText(R.string.card_read);

                                temp += "\nCard Aid:" + mCard.getAid();
                                temp += "\nProvider:" + mCard.getType();
                                if (mCard.getExpireDate()!= null) {
                                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                                    temp += "\nExpires: " + df.format(mCard.getExpireDate());
                                }
                                temp += "\nPin tries left: " + mCard.getLeftPinTry();

                                mReadCard = mCard;
                            } else if (mCard.isNfcLocked()) {
                                temp += getText(R.string.nfc_locked);
                            }
                        } else {
                            temp +=getText(R.string.error_card_unknown);

                        }
                    } else {
                        temp +=getText(R.string.error_communication_nfc);
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle("Lestur korts");
                    builder.setMessage(temp);

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

    public void backToHomeScreen() {
        // Select first menu
       /* mDrawerListView.performItemClick(mDrawerListView, 0, mDrawerListView.getItemIdAtPosition(0));
        // Close Drawer
        mDrawerLayout.closeDrawer(mDrawerListView);*/

        //todo: Hvað skal gera hér?
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

        TextView tvUpphaed = (TextView)findViewById(R.id.tViewUpphaed);
        tvUpphaed.setText("10010 kr.");



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
}
