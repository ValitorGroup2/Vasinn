package gens.com.vasinn.controllers;

import java.util.Date;
import java.util.List;

import gens.com.vasinn.repos.TransactionRepo;
import gens.com.vasinn.repos.objects.Transaction;

/**
 * Created by Gudjon on 6.5.2015.
 */

public class TransactionController {
    private TransactionRepo transRepo;

    public TransactionController() {
        transRepo = new TransactionRepo();
    }

    /*
        Returns a list of transactions
        from is a zero based index of the first transaction to list
        to is a zero based index of the first transaction to list
    */
    public List<Transaction> getRange(int from, int to){

        return transRepo.getRange(from, to);
    }

    public Transaction findById(int id)
    {
        return transRepo.findById(id);
    }


    public Transaction get(int position) {

        return transRepo.get(position);
    }
    public boolean updateTransaction(Transaction updateMe) {
        Transaction old = transRepo.get(updateMe.getId());
        if (old == null)
            return false;

        return transRepo.set(updateMe);
    }


    /*public Transaction add(Transaction addMe)
    {
        return transRepo.add(new Transaction(0, addMe.getDate(), addMe.getAmount(), addMe.getUserName(), addMe.getCard()));
    }*/
     public Transaction add(double amount, String userName, String card, boolean isRefundabble)
    {
        return transRepo.add(new Transaction(0, new Date(System.currentTimeMillis()), amount, userName, card, isRefundabble));
    }

    public int getSize() {
        return transRepo.size();
    }
}
