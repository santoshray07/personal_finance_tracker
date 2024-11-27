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
    public MutableLiveData<RealmResults<Transaction>> categoriesTransactions = new MutableLiveData<>();

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



    public void getTransactions(Calendar calendar, String type){
        this.calendar = calendar;

        RealmResults<Transaction> newTransactions = null;

        if(Constants.SELECTED_TAB_STATS==Constants.DAILY) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            newTransactions = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .equalTo("type", type)
                    .findAll();

            transactions.setValue(newTransactions);
        } else if(Constants.SELECTED_TAB_STATS== Constants.MONTHLY){
            calendar.set(Calendar.DAY_OF_MONTH,0);
            Date startTime = calendar.getTime();

            calendar.add(Calendar.MONTH, 1);
            Date endTime = calendar.getTime();

            newTransactions = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .equalTo("type", type)
                    .findAll();

            categoriesTransactions.setValue(newTransactions);
        }
    }


    public void getTransactions(Calendar calendar){
        this.calendar = calendar;
        double income = 0;
        double expense = 0;
        double amount = 0;
        RealmResults<Transaction> newTransactions = null;

        if(Constants.SELECTED_TAB==Constants.DAILY) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

             newTransactions = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .findAll();
            income = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .equalTo("type", Constants.INCOME)
                    .sum("amount").doubleValue();

            expense = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .equalTo("type", Constants.EXPENSE)
                    .sum("amount").doubleValue();

            amount = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .sum("amount").doubleValue();

            totalIncome.setValue(income);
            totalExpense.setValue(expense);
            totalAmount.setValue(amount);

            transactions.setValue(newTransactions);
        } else if(Constants.SELECTED_TAB == Constants.MONTHLY){
            calendar.set(Calendar.DAY_OF_MONTH,0);
            Date startTime = calendar.getTime();

            calendar.add(Calendar.MONTH, 1);
            Date endTime = calendar.getTime();

            newTransactions = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .findAll();

            income = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .equalTo("type", Constants.INCOME)
                    .sum("amount")
                    .doubleValue();

            expense = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .equalTo("type", Constants.EXPENSE)
                    .sum("amount")
                    .doubleValue();

            amount = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date",endTime)
                    .sum("amount")
                    .doubleValue();

            totalIncome.setValue(income);
            totalExpense.setValue(expense);
            totalAmount.setValue(amount);

            transactions.setValue(newTransactions);
        }
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
