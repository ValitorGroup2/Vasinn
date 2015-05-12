package gens.com.vasinn.dialogs;

import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
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
import gens.com.vasinn.repos.objects.Transaction;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReportDialog.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReportDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportDialog extends DialogFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mDialogTitle;
    private int mTransactionID;
    private Transaction mTransaction;
    VasiApplication vasinn;

    Button btnConfirm, btnCancel;
    EditText edtReport;

    @Override
    public void show(android.app.FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    //private OnFragmentInteractionListener mListener;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialog_fragment_report, null);
        btnConfirm = (Button) view.findViewById(R.id.btnSendDialogReport);
        btnCancel = (Button) view.findViewById(R.id.btnCancelSendReport);
        edtReport = (EditText) view.findViewById(R.id.edtTextReport);
        btnConfirm.requestFocus();
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        vasinn = ((VasiApplication) this.getActivity().getApplication());

        if (getArguments() != null) {
            mDialogTitle = getArguments().getString(ARG_PARAM1);
            mTransactionID = getArguments().getInt(ARG_PARAM2);
            mTransaction = vasinn.getTransactionController().findById(mTransactionID);
        }


        loadParameters();

        return view;
    }

    private void loadParameters() {
        getDialog().setTitle(mDialogTitle);

        if (mTransaction != null)
        {
            ((TextView)view.findViewById(R.id.tViewReportTransactionID)).setText(mTransaction.getIdString());
            ((TextView)view.findViewById(R.id.tViewReportTransactionID)).setText(mTransaction.getDateTimeString());
            ((TextView)view.findViewById(R.id.tViewReportTransactionCard)).setText(mTransaction.getCard());
            ((TextView)view.findViewById(R.id.tViewReportTransactionAmount)).setText(mTransaction.getAmountString());
            ((TextView)view.findViewById(R.id.tViewReportTransactionUsername)).setText(mTransaction.getUserName());

        }
        else{
            ((TextView)view.findViewById(R.id.tViewReportTransactionID)).setText("");
            ((TextView)view.findViewById(R.id.tViewReportTransactionID)).setText("");
            ((TextView)view.findViewById(R.id.tViewReportTransactionCard)).setText("");
            ((TextView)view.findViewById(R.id.tViewReportTransactionAmount)).setText("");
            ((TextView)view.findViewById(R.id.tViewReportTransactionUsername)).setText("Færsla fannst ekki");
        }



    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param dialogTitle   Parameter 1.  Title of the dialog
     * @param transactionID Parameter 2.  ID of the transaction to report
     * @return A new instance of fragment ReportDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportDialog newInstance(String dialogTitle, int transactionID) {
        ReportDialog fragment = new ReportDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, dialogTitle);
        args.putInt(ARG_PARAM2, transactionID);
        fragment.setArguments(args);
        return fragment;
    }

    public ReportDialog() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnSendDialogReport)
        {
            //let's send the report;
            String email = vasinn.getUserController().findAdminEmail(vasinn.getLoggedInUsername());
            if (email != null)
            {

                try {
                    new SendEmailAsyncTask(email,
                            vasinn.getString(R.string.email_report_subject),
                            vasinn.getString(R.string.email_report_body_before) +
                                    "\n" + edtReport.getText().toString() +
                                    "\n-------------------------------------------------\n" +
                                    vasinn.getString(R.string.email_report_body_after) +
                                    "\n" + mTransaction.toString(true) +
                                    "\n-------------------------------------------------\n" +
                                    vasinn.getString(R.string.your_notifycations) +
                                    " : "+ vasinn.getString(R.string.url_notifycation_link)
                    ).execute();
//email_receipt_sent_ok
                    Toast.makeText(getActivity(), vasinn.getString(R.string.email_report_sent_ok), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(getActivity(), vasinn.getString(R.string.email_report_sent_fail), Toast.LENGTH_LONG).show();
                }
                dismiss();
            }
        }
        else
        {
            //let's close the dialog;
            dismiss();
        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
