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

    public TransactionController() {
        transRepo = new TransactionRepo();
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

    public Transaction add(String cardNumber, double amount, String userName, boolean isRefundabble)
    {
        return transRepo.add(new Transaction(0, cardNumber, new Date(System.currentTimeMillis()), amount, userName, isRefundabble));
    }

    public int getSize() {
        return transRepo.size();
    }

    public List<Transaction> getRangeByUser(int from, int to, String loggedInUsername) {
        List<Transaction> allTrans = transRepo.getRange(from, to);
        List<Transaction> myTrans = new ArrayList<>();

        for (int i = 0; i < allTrans.size(); i++) {
            Transaction currTrans = allTrans.get(i);
            if (allTrans.get(i).getUsername().equals(loggedInUsername)) {
                myTrans.add(currTrans);
            }
        }

        return myTrans;
    }
}
