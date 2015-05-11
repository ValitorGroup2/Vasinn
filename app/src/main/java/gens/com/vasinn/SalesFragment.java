package gens.com.vasinn;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    VasiApplication vasi;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.sales_layout, container, false);
        listView = (ListView)rootview.findViewById(R.id.lViewTransactions);

        vasi = ((VasiApplication) this.getActivity().getApplication());


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Transaction item = (Transaction) adapter.getItem(position);

                Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.TRANSACTION_KEY_ID), item.getId());

                Intent intent = new Intent(getActivity(), TransactionActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        return rootview;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadTransactions();
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    public void loadTransactions(){

        TransactionController transCon = vasi.getTransactionController();
        //List<Transaction> range = transCon.getRange(transCon.getSize()-1, 0);
        List<Transaction> range = transCon.getRangeByUser(transCon.getSize()-1, 0, vasi.getLoggedInUsername());
        adapter = new TransactionsAdapter(rootview.getContext(), R.layout.row_layout_transaction);
        listView.setAdapter(adapter);
        for(int i = 0; i < range.size(); i++)
        {
            adapter.add(range.get(i));
        }
    }
}
