package gens.com.vasinn.repos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gens.com.vasinn.repos.objects.Transaction;

/**
 * Created by Gudjon on 6.5.2015.
 */
public class TransactionRepo {
    private List<Transaction> transactions = new ArrayList<Transaction>();


    public TransactionRepo() {
                                                    //yyy mm  dd  hh  mm ss
        transactions.add(new Transaction( 1, new Date(2015, 5,  6, 17, 15,  0),   1540, "gudjon" ));
        transactions.add(new Transaction( 2, new Date(2015, 5,  6, 12, 15,  0),  23800, "gudny" ));
        transactions.add(new Transaction( 3, new Date(2015, 5,  5, 11, 35, 15), 212844, "aegir" ));
        transactions.add(new Transaction( 4, new Date(2015, 5,  5, 10, 25,  0),  28401, "karl" ));
        transactions.add(new Transaction( 5, new Date(2015, 5,  3, 10, 25, 30),    507, "aegir" ));
        transactions.add(new Transaction( 6, new Date(2015, 5,  1, 14, 30, 21),   1240, "aegir" ));
        transactions.add(new Transaction( 7, new Date(2015, 4, 30, 15, 32, 11),  74124, "gretar" ));
        transactions.add(new Transaction( 8, new Date(2015, 4, 30, 15, 35, 10), -74124, "gretar" ));
        transactions.add(new Transaction( 9, new Date(2015, 4, 10, 12, 15,  0),   1234, "gudjon" ));
        transactions.add(new Transaction(10, new Date(2015, 4,  3, 12, 15,  0),   8870, "gudjon" ));
        transactions.add(new Transaction(11, new Date(2015, 4,  3, 42, 15,  0),   1520, "aegir" ));
        transactions.add(new Transaction(12, new Date(2015, 4,  1, 16, 38,  1),   1254, "aegir" ));
    }

    public void add(Transaction addMe)
    {
        int id = transactions.size() + 1;
        transactions.add(addMe);
    }

    /*  from og to are (til og með)
        from is a zero based index of the first transaction to list
        to is a zero based index of the first transaction to list
    */
    public List<Transaction> getRange(int from, int to){
        if (from < 0 || to > transactions.size() - 1)
            throw new IndexOutOfBoundsException("Transaction Range is out of bounds");

        List<Transaction> retTrans = new ArrayList<Transaction>(from - (to  + 1));

        for(int i = from; i < to + 1; i++) {
            retTrans.add(transactions.get(i));
        }

        return retTrans;
    }

}