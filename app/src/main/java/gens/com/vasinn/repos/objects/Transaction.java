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

    private Date date;
    private int id;
    private String userName;
    private boolean isRefundable;
    private String card; //this is not needed, we could calc this from the card number.
    private double amount;

    //returns the last 4 digits in a card number
    public String getCardNumberSafe() {

        if (cardNumber.length()<6)
            return "";  //invalid card-number

        String ret = "";
        for(int i = 1; i < cardNumber.length() - 3; i++) {
            ret += "*";
            if (i % 4 == 0)
                ret += "-";
        }
        ret += cardNumber.substring(cardNumber.length() - 4);
        return ret;
    }
    public String getCardNumber() {

        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    private String cardNumber;

    public void setAmount(double amount) {this.amount = amount;}
    public void setCard(String card) {this.card = card;}
    public void setIsRefundable(boolean isRefundable) {this.isRefundable = isRefundable;}

    public Date getDate() {return date;}
    public boolean isRefundable() {return isRefundable;}
    public String getCard() {return card;}
    public double getAmount() {return amount;}
    public int getId() {return id;}
    public String getUserName() {return userName;}
    public String getIdString() {return Integer.toString(id);}
    public String getAmountString() {

        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        DecimalFormat df = new DecimalFormat("#,##0", new DecimalFormatSymbols(new Locale("is", "IS", "")));
        return df.format(amount) + " kr.";
    }

    public Transaction(int id, String cardNumber, Date date, double amount, String userName, String card, boolean isRefundable) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.date = date;
        this.amount = amount;
        this.userName = userName;
        this.card = card;
        this.isRefundable = isRefundable;
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



    //returns a string containging all information about a transaction
    public String toString(boolean includeRefundable){

       String ret = "Kortanúmer: " + getCardNumberSafe() +
                  "\nTími: " + getDateTimeString() +
                  "\nStarfsmaður: " + getUserName();
        if (includeRefundable)
            ret +="\nMá endurgreiða: " + (isRefundable() ? "Já" : "Nei");

        ret +=
                  "\nKort: " + getCard() +
                  "\nUpphæð: " + getAmountString();

        return ret;
    }
}
