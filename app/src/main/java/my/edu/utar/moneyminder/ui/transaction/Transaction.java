package my.edu.utar.moneyminder.ui.transaction;


// model class

public class Transaction {
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

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

}
