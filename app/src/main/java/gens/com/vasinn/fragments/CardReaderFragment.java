package gens.com.vasinn.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import gens.com.vasinn.R;
import gens.com.vasinn.VasiApplication;
import gens.com.vasinn.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CardReaderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CardReaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardReaderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private float  mParamAmount;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CardReaderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CardReaderFragment newInstance(String param1, float amount) {
        CardReaderFragment fragment = new CardReaderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putFloat(ARG_PARAM2, amount);
        fragment.setArguments(args);
        return fragment;
    }

    public CardReaderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParamAmount = getArguments().getFloat(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_reader, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

   /* @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

   /* @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    public String amountToString(double amount) {

        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');

        DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("is", "IS", "")));

        return df.format(amount) + " kr.";
    }

    public void loadAmount(){
       /* SharedPreferences mySharedPreferences;

        mySharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.VASINN_PREFERENCE), Activity.MODE_PRIVATE);

        // Retrieve the saved values.
        float ret = mySharedPreferences.getFloat(getString(R.string.VASINN_PREFERENCE_AMOUNT), 0);
        //mySharedPreferences.edit().putFloat(getString(R.string.VASINN_PREFERENCE_AMOUNT), 0);*/
        String text = getString(R.string.transaction_amount) + ": ";
        text+=amountToString(mParamAmount);
        if(mParamAmount == 0)
        {   //no ammount to display so let's go to posi
            ((MainActivity)getActivity()).onNavigationDrawerItemSelected(0);
        }
        mParamAmount = 0;

        ((TextView)getView().findViewById(R.id.tViewReaderAmount)).setText(text);

    }

    @Override
    public void onStart() {
        super.onStart();
        loadAmount();

    }

}
