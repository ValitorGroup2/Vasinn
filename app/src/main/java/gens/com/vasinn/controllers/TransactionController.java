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

    //returns the size of the repo
    public int getSize(){

        return transRepo.size();
    }

    public Transaction findById(int id)
    {
        return transRepo.findById(id);
    }

    public void add(Date date, double amount, String userName) {

        this.add(new Transaction(0, date, amount, userName));
    }

    public void add(Transaction addMe)
    {
        transRepo.add(new Transaction(0, addMe.getDate(), addMe.getAmount(), addMe.getUserName()));
    }
}
