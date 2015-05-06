package gens.com.vasinn.repos.objects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gudjon on 6.5.2015.
 */
public class Transaction extends Object {


    public Date getDate() {
        return date;
    }

    private Date date;
    private int id;
    private String userName;

    public double getAmount() {
        return amount;
    }

    private double amount;


    public Transaction(int id, Date date, double amount, String userName) {
        this.date = date;
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
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        return df.format(this.date);
    }

    @Override
    public boolean equals(Object obj) {

        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }

        Transaction his = (Transaction) obj;

        return this.amount == his.amount &&
                this.date.equals(his.date) &&
                this.id == his.id &&
                this.userName.equals(his.userName);
    }
}
