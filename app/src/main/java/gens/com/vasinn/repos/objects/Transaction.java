package gens.com.vasinn.repos.objects;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    private String card;

    public double getAmount() {
        return amount;
    }
    public String getAmountString() {

        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');

        DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("is", "IS", "")));

        return df.format(amount) + " kr.";
    }

    private double amount;


    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Transaction(int id, Date date, double amount, String userName, String card) {
        this.date = date;
        this.id = id;
        this.amount = amount;
        this.userName = userName;
        this.card = card;
    }

    public int getId() {
        return id;
    }
    public String getIdString() {
        return Integer.toString(id);
    }

    public String getUserName() {
        return userName;
    }

    public String getDateString() {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        return df.format(this.date);
    }

    public String getDateTimeString() {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
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
