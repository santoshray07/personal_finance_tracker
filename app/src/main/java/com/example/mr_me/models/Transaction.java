//package com.example.mr_me.models;
//
//import java.util.Date;
//
//import io.realm.RealmObject;
//import io.realm.annotations.PrimaryKey;
//
////import io.realm.RealmObject;
////import io.realm.annotations.PrimaryKey;
//
////public class Transaction extends RealmObject {
//public class  Transaction extends RealmObject {
//    private String type, category, account, note;
//    private Date date;
//    private double amount;
//
//    @PrimaryKey
//    private long id;
//
//    public Transaction() {
//    }
//
//    public Transaction(String type, String category, String account, String note, Date date, double amount, long id) {
//        this.type = type;
//        this.category = category;
//        this.account = account;
//        this.note = note;
//        this.date = date;
//        this.amount = amount;
//        this.id = id;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
//
//    public String getAccount() {
//        return account;
//    }
//
//    public void setAccount(String account) {
//        this.account = account;
//    }
//
//    public String getNote() {
//        return note;
//    }
//
//    public void setNote(String note) {
//        this.note = note;
//    }
//
//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }
//
//    public double getAmount() {
//        return amount;
//    }
//
//    public void setAmount(double amount) {
//        this.amount = amount;
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//}

package com.example.mr_me.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Transaction extends RealmObject {
    @PrimaryKey
    private long id;

    private String type;
    private String category;
    private String paymentMethod; // Changed to match migration
    private String description;   // Changed to match migration
    private Date date;
    private double amount;
    private long timestamp;

    public Transaction() {
    }

    public Transaction(String type, String category, String paymentMethod, String description, Date date, double amount, long id) {
        this.type = type;
        this.category = category;
        this.paymentMethod = paymentMethod;
        this.description = description;
        this.date = date;
        this.amount = amount;
        this.id = id;
        this.timestamp = date.getTime(); // Automatically set timestamp
    }

    // Getter and Setter methods...
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

}
