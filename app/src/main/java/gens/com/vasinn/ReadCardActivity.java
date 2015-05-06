package gens.com.vasinn;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.github.devnied.emvnfccard.exception.CommunicationException;
import com.github.devnied.emvnfccard.fragment.IRefreshable;
import com.github.devnied.emvnfccard.model.EmvCard;
import com.github.devnied.emvnfccard.parser.EmvParser;
import com.github.devnied.emvnfccard.parser.IProvider;
import com.github.devnied.emvnfccard.utils.AtrUtils;
import com.github.devnied.emvnfccard.utils.NFCUtils;
import com.github.devnied.emvnfccard.utils.SimpleAsyncTask;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collection;

import com.emvnfccard.provider.Provider;

import fr.devnied.bitlib.BytesUtils;


public class ReadCardActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NFCUtils mNfcUtils;
    private Provider mProvider = new Provider();
    private EmvCard mReadCard;
    private byte[] lastAts;

    /**
     * Reference for refreshable content
     */
    private WeakReference<IRefreshable> mRefreshableContent;


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_card);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // init NfcUtils
        mNfcUtils = new NFCUtils(this);

        /*IProvider prov = new YourProvider();
// Create parser (true for contactless false otherwise)
        EmvParser parser = new EmvParser(prov, true);
// Read card
        try {
            EmvCard card = parser.readEmvCard();
            Toast.makeText(getApplicationContext(), "Card AID:"+card.getAid(), Toast.LENGTH_LONG).show();
        } catch (CommunicationException e) {
            e.printStackTrace();
        }*/



    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
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
                mTitle = getString(R.string.title_section3);
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
            getMenuInflater().inflate(R.menu.read_card, menu);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
            View rootView = inflater.inflate(R.layout.fragment_read_card, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((ReadCardActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        final Tag mTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (mTag != null) {

            new SimpleAsyncTask() {


                // Tag comm

                private IsoDep mTagcomm;


                // Emv Card

                private EmvCard mCard;


                // Boolean to indicate exception

                private boolean mException;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    // backToHomeScreen();
                    mProvider.getLog().setLength(0);
                    // Show dialog
                    Toast.makeText(getApplicationContext(), getString(R.string.card_reading), Toast.LENGTH_LONG).show();
                    /*if (mDialog == null) {
                        mDialog = ProgressDialog.show(HomeActivity.this, getString(R.string.card_reading),
                                getString(R.string.card_reading_desc), true, false);
                    } else {
                        mDialog.show();
                    }*/
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

                @Override
                protected void doInBackground() {

                    mTagcomm = IsoDep.get(mTag);
                    if (mTagcomm == null) {
                        //CroutonUtils.display(HomeActivity.this, getText(R.string.error_communication_nfc), CoutonColor.BLACK);

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

                private void refreshContent() {
                    if (mRefreshableContent != null && mRefreshableContent.get() != null) {
                        mRefreshableContent.get().update();
                    }
                }

                @Override
                protected void onPostExecute(final Object result) {
                    // close dialog
                    /*if (mDialog != null) {
                        mDialog.cancel();
                    }*/
                    String temp = "";
                    if (!mException) {
                        if (mCard != null) {
                            if (StringUtils.isNotBlank(mCard.getCardNumber())) {
                                temp += getText(R.string.card_read);
                                //CroutonUtils.display(HomeActivity.this, getText(R.string.card_read), CoutonColor.GREEN);
                                mReadCard = mCard;
                            } else if (mCard.isNfcLocked()) {
                                temp += getText(R.string.nfc_locked);
                                //CroutonUtils.display(HomeActivity.this, getText(R.string.nfc_locked), CoutonColor.ORANGE);
                            }
                        } else {
                            temp +=getText(R.string.error_card_unknown);
                            //CroutonUtils.display(HomeActivity.this, getText(R.string.error_card_unknown), CoutonColor.BLACK);
                        }
                    } else {
                        temp +=getText(R.string.error_communication_nfc);
                        //CroutonUtils.display(HomeActivity.this, getResources().getText(R.string.error_communication_nfc), CoutonColor.BLACK);
                    }

                    Toast.makeText(getApplicationContext(), "Garg: " +temp , Toast.LENGTH_LONG).show();
                    refreshContent();
                }

            }.execute();
        }

    }

    /**
     * Get the last ATS
     *
     * @return the last card ATS
     */
    public byte[] getLastAts() {
        return lastAts;
    }

}
