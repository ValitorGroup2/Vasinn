package gens.com.vasinn.repos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import gens.com.vasinn.repos.objects.Transaction;

/**
 * Created by Gudjon on 6.5.2015.
 */
public class TransactionRepo {
    private List<Transaction> transactions = new ArrayList<Transaction>();

    public TransactionRepo() {

        transactions.add(new Transaction( 1,"4444444444444444", new Date((new GregorianCalendar(2013, 5,  6, 13, 24, 56)).getTimeInMillis()),   1540.10, "gudjon","Visa", true));
        transactions.add(new Transaction( 2,"5555555555555555", new Date((new GregorianCalendar(2015, 5,  6, 12, 15,  0)).getTimeInMillis()),  23800.5, "gudny", "Mastercard", true));
        transactions.add(new Transaction( 3,"4444444444444444", new Date((new GregorianCalendar(2015, 5,  5, 11, 35, 15)).getTimeInMillis()), 212844.51, "aegir", "Visa", true));
        transactions.add(new Transaction( 4,"4444444444444444", new Date((new GregorianCalendar(2015, 5,  5, 10, 25,  0)).getTimeInMillis()),  28401, "karl",  "American express",true ));
        transactions.add(new Transaction( 5,"4444444444444444", new Date((new GregorianCalendar(2015, 5,  3, 10, 25, 30)).getTimeInMillis()),    507, "aegir", "Mastercard",true ));
        transactions.add(new Transaction( 6,"4444444444444444", new Date((new GregorianCalendar(2015, 5,  1, 14, 30, 21)).getTimeInMillis()),   1240, "aegir", "Visa",true ));
        transactions.add(new Transaction( 7,"4444444444444444", new Date((new GregorianCalendar(2015, 4, 30, 15, 32, 11)).getTimeInMillis()),  74124, "gretar","Mastercard",false));
        transactions.add(new Transaction( 8,"4444444444444444", new Date((new GregorianCalendar(2015, 4, 30, 15, 35, 10)).getTimeInMillis()), -74124, "gretar","Visa",false ));
        transactions.add(new Transaction( 9,"4444444444444444", new Date((new GregorianCalendar(2015, 4, 10, 12, 15,  0)).getTimeInMillis()),   1234, "gudjon","Visa",true ));
        transactions.add(new Transaction(10,"4444444444444444", new Date((new GregorianCalendar(2015, 4,  3, 12, 15,  0)).getTimeInMillis()),   8870, "gudjon","Mastercard",true ));
        transactions.add(new Transaction(11,"4444444444444444", new Date((new GregorianCalendar(2015, 4,  3, 42, 15,  0)).getTimeInMillis()),   1520, "aegir", "Visa",true ));
        transactions.add(new Transaction(12,"4444444444444444", new Date((new GregorianCalendar(2015, 4,  1, 16, 38,  1)).getTimeInMillis()),   1254, "aegir", "Visa",true ));

    }

    public int size(){

        return transactions.size();
    }

    public Transaction get(int index){

        return transactions.get(index);
    }
    public boolean set(Transaction updatedTransaction){
        int index = transactions.indexOf(updatedTransaction);
        if (index < 0 ) return false;

        return transactions.set(index, updatedTransaction) != null;
    }



    public Transaction findById(int id){

        if (id < 0)
            return null;
        for(int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getId() == id)
                return transactions.get(i);
        }
        return null;
    }

    public Transaction add(Transaction addMe)
    {
        int id = transactions.size() + 1;

        transactions.add(new Transaction(id, addMe.getCardNumber(), addMe.getDate(), addMe.getAmount(), addMe.getUserName(), addMe.getCard(), addMe.isRefundable()));
        return get(transactions.size() - 1);
    }

    /*
        from is a zero based index of the first transaction to put in the returned range
        to is a zero based index of the last transaction to put in the returned range

        to is allowed to be smaller than from, so you can get data descending.
    */
    public List<Transaction> getRange(int from, int to){

        if (from < 0) from = 0;
        if (to < 0) to = 0;
        int last = transactions.size() - 1;

        if (last < 0)
            return null;

        if (from > last) from = last;
        if (to  > last)  to = last;

        List<Transaction> retTrans = new ArrayList<Transaction>();

        if (from == to){
            //only one record to return
            retTrans.add(transactions.get(from));
            return retTrans;
        }

        if (from < to){
            for(int i = from; i < to + 1; i++) {
                retTrans.add(transactions.get(i));
            }
        }
        else{ //to is less than from
            for(int i = from; i > to - 1; i--) {
                retTrans.add(transactions.get(i));
            }
        }

        return retTrans;
    }
}