package gens.com.vasinn.repos.objects;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import gens.com.vasinn.constants.CardConstants;

/**
 * Created by Gudjon on 6.5.2015.
 */
public class Transaction extends Object {

    private Date date;
    private int id;
    private String username;
    private boolean isRefundable;
    private double amount;
    private String cardNumber;

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


    public void setAmount(double amount) {this.amount = amount;}
    public void setIsRefundable(boolean isRefundable) {this.isRefundable = isRefundable;}

    public Date getDate() {return date;}
    public boolean isRefundable() {return isRefundable;}
    public String getCard() {return cardTypeToString(extractCardType(this.getCardNumber()));}
    public double getAmount() {return amount;}
    public int getId() {return id;}
    public String getUsername() {return username;}
    public String getIdString() {return Integer.toString(id);}
    public String getAmountString() {

        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        DecimalFormat df = new DecimalFormat("#,##0", new DecimalFormatSymbols(new Locale("is", "IS", "")));
        return df.format(amount) + " kr.";
    }

    public Transaction(int id, String cardNumber, Date date, double amount, String username, boolean isRefundable) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.date = date;
        this.amount = amount;
        this.username = username;
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
                this.username.equals(his.username);
    }



    //returns a string containging all information about a transaction
    public String toString(boolean includeRefundable){

       String ret = "Kortanúmer: " + getCardNumberSafe() +
                  "\nTími: " + getDateTimeString() +
                  "\nStarfsmaður: " + getUsername();
        if (includeRefundable)
            ret +="\nMá endurgreiða: " + (isRefundable() ? "Já" : "Nei");

        ret +=
                  "\nKort: " + getCard() +
                  "\nUpphæð: " + getAmountString();

        return ret;
    }

    public int extractCardType(String number){

        if (number == null || number.length() < 15)
            return CardConstants.UNKNOWN;

        String MAESTRO = "(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\\d{8}|(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\\d{9}|(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\\d{10}|(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\\d{11}|(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\\d{12}|(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\\d{13}|(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\\d{14}|(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\\d{15}$";
        String MASTERCARD = "^(?!.*(?:(?:5018|5020|5038\\d{12})))[5][0-5].{14}$";
        String SOLO = "(6334|6767)\\d{12}|(6334|6767)\\d{14}|(6334|6767)\\d{15}";
        String SWITCH = "(4903|4905|4911|4936|6333|6759)\\d{12}|(4903|4905|4911|4936|6333|6759)\\d{14}|(4903|4905|4911|4936|6333|6759)\\d{15}|(564182|633110)\\d{10}|(564182|633110)\\d{12}|(564182|633110)\\d{13}$";
        String VISA = "^(?!.*(?:(?:4026|4405|4508|4844|4913|4917)\\d{12}|417500\\d{10}))4\\d{15}$";
        String VISA_ELECTRON = "(4026|4405|4508|4844|4913|4917)\\d{12}|417500\\d{10}$";


        if (Pattern.compile(MAESTRO).matcher(number).matches())
            return CardConstants.MAESTRO;
        if (Pattern.compile(MASTERCARD).matcher(number).matches())
            return CardConstants.MASTERCARD;
        if (Pattern.compile(SOLO).matcher(number).matches())
            return CardConstants.SOLO;
        if (Pattern.compile(SWITCH).matcher(number).matches())
            return CardConstants.SWITCH;
        if (Pattern.compile(VISA_ELECTRON).matcher(number).matches())
            return CardConstants.VISA_ELECTRON;
        if (Pattern.compile(VISA).matcher(number).matches())
            return CardConstants.VISA;

        return CardConstants.UNKNOWN;

    }
    public String cardTypeToString(int cardType){


        switch(cardType) {
            case CardConstants.MAESTRO:
                return "Maestro";
            case CardConstants.MASTERCARD:
                return "Mastercard";
            case CardConstants.SOLO:
                return "Solo";
            case CardConstants.SWITCH:
                return "Switch";
            case CardConstants.VISA_ELECTRON:
                return "Visa electron";
            case CardConstants.VISA:
                return "Visa";
        }
        return null;
    }
}
