package gens.com.vasinn.repos.objects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gudjon on 6.5.2015.
 */
public class Transaction extends Object {


    private Date date;
    private int id;
    private String userName;
    private double amount;


    public Transaction(int id, Date m_date, double amount, String userName) {
        this.date = m_date;
        this.id = id;
        this.amount = amount;
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getDateString() {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        return df.format(this.date);
    }

    public String getDateTimeString() {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        return df.format(this.date);
    }

    @Override
    public boolean equals(Object obj) {
        Transaction my  = this;
        Transaction his = (Transaction) obj;

        if (my.amount == his.amount  &&
            my.date.equals(his.date) &&
            my.id == his.id          &&
            my.userName.equals(his.userName))
            return true;
        else
            return false;
    }
}
