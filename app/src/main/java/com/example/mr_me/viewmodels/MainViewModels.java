package com.example.mr_me.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.mr_me.models.Transaction;
import com.example.mr_me.utils.Constants;
import com.example.mr_me.views.activities.MyRealmMigration;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainViewModels extends AndroidViewModel {
    public MutableLiveData<RealmResults<Transaction>> transactions = new MutableLiveData<>();
    Calendar calendar;

    public MutableLiveData<Double> totalIncome = new MutableLiveData<>();
    public MutableLiveData<Double> totalExpense = new MutableLiveData<>();
    public MutableLiveData<Double> totalAmount= new MutableLiveData<>();

    Realm realm;
    public MainViewModels(@NonNull Application application){
        super(application);
        Realm.init(application);
        setUpDatabase();
    }

    void setUpDatabase() {
        Realm.init(this.getApplication());

        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(1) // Increment when schema changes
                .migration(new MyRealmMigration()) // Handle migration
                .build();

        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
    }

    public void getTransactions(Calendar calendar){
        this.calendar = calendar;
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        RealmResults<Transaction> newTransactions = realm.where(Transaction.class)
                .greaterThanOrEqualTo("date", calendar.getTime())
                .lessThan("date", new Date(calendar.getTime().getTime()+(24*60*60*1000)))
                .findAll();
        double income = realm.where(Transaction.class)
                .greaterThanOrEqualTo("date", calendar.getTime())
                .lessThan("date", new Date(calendar.getTime().getTime()+(24*60*60*1000)))
                .equalTo("type", Constants.INCOME)
                .sum("amount").doubleValue();

        double expense = realm.where(Transaction.class)
                .greaterThanOrEqualTo("date", calendar.getTime())
                .lessThan("date", new Date(calendar.getTime().getTime()+(24*60*60*1000)))
                .equalTo("type", Constants.EXPENSE)
                .sum("amount").doubleValue();

        double amount = realm.where(Transaction.class)
                .greaterThanOrEqualTo("date", calendar.getTime())
                .lessThan("date", new Date(calendar.getTime().getTime()+(24*60*60*1000)))
                .sum("amount").doubleValue();

        totalIncome.setValue(income);
        totalExpense.setValue(expense);
        totalAmount.setValue(amount);

        transactions.setValue(newTransactions);
    }

    public void deleteTransactions(Transaction transaction){
        realm.beginTransaction();
        transaction.deleteFromRealm();
        realm.commitTransaction();
        getTransactions(calendar);
    }

    public void addTransactions(Transaction transaction){
        realm.beginTransaction();
        try {
            realm.copyToRealmOrUpdate(transaction);
            realm.commitTransaction();
        } catch (Exception e) {
            realm.cancelTransaction();
            e.printStackTrace();
        }
    }
}
