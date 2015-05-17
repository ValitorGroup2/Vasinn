package gens.com.vasinn.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gens.com.vasinn.repos.TransactionRepo;
import gens.com.vasinn.repos.objects.Transaction;

/**
 * Created by Gudjon on 6.5.2015.
 */

public class TransactionController {
    private TransactionRepo transRepo;

    //add a transaction fake delay to reprecent the delay, when taalking to a real server

    private void fakeDelay(){
        /*try {
            //todo: to simulate transaction delay dlgWait
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

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

        fakeDelay();

        Transaction old = transRepo.get(updateMe.getId());
        if (old == null)
            return false;

        return transRepo.set(updateMe);
    }


    /*public Transaction add(Transaction addMe)
    {
        return transRepo.add(new Transaction(0, addMe.getDate(), addMe.getAmount(), addMe.getUserName(), addMe.getCard()));
    }*/
     public Transaction add(String cardNumber, double amount, String userName, String card, boolean isRefundabble)
    {
        fakeDelay();
        return transRepo.add(new Transaction(0, cardNumber, new Date(System.currentTimeMillis()), amount, userName, card, isRefundabble));
    }

    public int getSize() {
        return transRepo.size();
    }

    public List<Transaction> getRangeByUser(int from, int to, String loggedInUsername) {
        List<Transaction> allTrans = transRepo.getRange(from, to);
        List<Transaction> myTrans = new ArrayList<>();

        for (int i = 0; i < allTrans.size(); i++) {
            Transaction currTrans = allTrans.get(i);
            if (allTrans.get(i).getUserName().equals(loggedInUsername)) {
                myTrans.add(currTrans);
            }
        }

        return myTrans;
    }
}
