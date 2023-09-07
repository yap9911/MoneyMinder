package my.edu.utar.moneyminder.ui.transaction;

import java.util.Date;

// model class

public class Transaction {
    private String Id;
    private String amount;
    private String category;
    private String date;
    private String note;

    // Constructor
    public Transaction(String Id, String amount, String category, String date, String note) {
        this.Id = Id;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.note = note;
    }

    public String getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getId() { return Id; }

    public String getNote() { return note; }


}
