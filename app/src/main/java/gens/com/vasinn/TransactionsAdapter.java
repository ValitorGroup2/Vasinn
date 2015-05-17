package gens.com.vasinn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gens.com.vasinn.repos.objects.Transaction;

/**
 * Created by Gudjon on 7.5.2015.
 */
public class TransactionsAdapter extends ArrayAdapter {

    List list = new ArrayList();
    public TransactionsAdapter(Context context, int resource) {
        super(context, resource);
    }

    static class DataHandler{
        TextView tViewDate;
        TextView tViewUser;
        TextView tViewAmount;
    }

    @Override
    public void add(Object object) {
       // super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {

        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row;
        row = convertView;
        DataHandler handler;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.row_layout_transaction, parent, false);
            handler = new DataHandler();
            handler.tViewDate   = (TextView)row.findViewById(R.id.tViewTopRowDate);
            handler.tViewUser   = (TextView)row.findViewById(R.id.tViewTopRowType);
            handler.tViewAmount = (TextView)row.findViewById(R.id.tViewTopRowAmount);

            /*if ( (position % 2) == 0)
            {
                row.setBackgroundColor(0xcccccccc);
            }
            else{
                row.setBackgroundColor(0xFFFFFFFF);
            }*/

            row.setTag(handler);
        }
        else{
            handler = (DataHandler)row.getTag();
        }

        Transaction transaction;
        transaction = (Transaction)this.getItem(position);
        handler.tViewDate.setText(transaction.getDateTimeString());
        handler.tViewUser.setText(transaction.getUserName());
        handler.tViewAmount.setText(transaction.getAmountString());

        return row;
    }
}
