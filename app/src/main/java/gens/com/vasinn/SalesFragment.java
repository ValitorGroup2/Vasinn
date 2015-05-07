package gens.com.vasinn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import gens.com.vasinn.controllers.TransactionController;
import gens.com.vasinn.repos.objects.Transaction;

/**
 * Created by Ægir Már Jónsson on 7.5.2015.
 */
public class SalesFragment extends Fragment {
    View rootview;

    ListView listView;
    TransactionsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.sales_layout, container, false);

        listView = (ListView)rootview.findViewById(R.id.lViewTransactions);

        VasiApplication vasi = ((VasiApplication) this.getActivity().getApplication());
        TransactionController transCon = vasi.getTransactionController();

        List<Transaction> range = transCon.getRange(transCon.getSize()-1, 0);
        adapter = new TransactionsAdapter(rootview.getContext(), R.layout.row_layout_transaction);
        listView.setAdapter(adapter);
        for(int i = 0; i < range.size(); i++)
        {
            adapter.add(range.get(i));
        }

        return rootview;
    }
}
