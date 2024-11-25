package com.example.mr_me.views.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu; // Import added for Menu

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mr_me.R;
import com.example.mr_me.adapters.TransactionsAdapter;
import com.example.mr_me.databinding.ActivityMainBinding;
import com.example.mr_me.models.Transaction;
import com.example.mr_me.utils.Constants;
import com.example.mr_me.utils.Helper;
import com.example.mr_me.views.fragments.AddTransactionFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    Calendar calendar;
//    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addTransactionBtn.setOnClickListener(c->{
            new AddTransactionFragment().show(getSupportFragmentManager(), null);
        });

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.orange));

//        setUpDatabase();

        setSupportActionBar(binding.toolBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Transactions");

        Constants.setCategories();

        calendar = Calendar.getInstance();
        updateDate();

        binding.nextDateBtn.setOnClickListener(c->{
            calendar.add(Calendar.DATE, 1);
            updateDate();
        });

        binding.prevDateBtn.setOnClickListener(c->{
            calendar.add(Calendar.DATE, -1);
            updateDate();
        });


        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ArrayList<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(Constants.INCOME,"Investment", "Cash", "Just testing", new Date(), 12000, 56498498));
        transactions.add(new Transaction(Constants.EXPENSE,"Business", "Bank", "Just testing", new Date(), 12000, 56498498));
        transactions.add(new Transaction(Constants.INCOME,"Gpay", "Cash", "Just testing", new Date(), 12000, 56498498));
        transactions.add(new Transaction(Constants.EXPENSE,"Loan", "Cash", "Just testing", new Date(), 12000, 56498498));

//        realm.beginTransaction();
//        try {
//            realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME, "Investment", "Cash", "Just testing", new Date(), 12000, new Date().getTime()));
//            realm.copyToRealmOrUpdate(new Transaction(Constants.EXPENSE, "Business", "Bank", "Just testing", new Date(), 12000, new Date().getTime()));
//            realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME, "Gpay", "Cash", "Just testing", new Date(), 12000, new Date().getTime()));
//            realm.copyToRealmOrUpdate(new Transaction(Constants.EXPENSE, "Loan", "Cash", "Just testing", new Date(), 12000, new Date().getTime()));
//            realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME, "Rent", "Other", "Just testing", new Date(), 12000, new Date().getTime()));
//            realm.commitTransaction();
//        } catch (Exception e) {
//            realm.cancelTransaction(); // Roll back changes if something goes wrong
//            e.printStackTrace();
//        }


        TransactionsAdapter transactionsAdapter = new TransactionsAdapter(this, transactions);
        binding.transactionsList.setLayoutManager(new LinearLayoutManager(this));
        binding.transactionsList.setAdapter(transactionsAdapter);
    }

    void updateDate (){
        binding.currentDate.setText(Helper.formatDate(calendar.getTime()));
    }

//    void setUpDatabase(){
//        Realm.init(this);
//        realm = Realm.getDefaultInstance();
//    }
//void setUpDatabase() {
//    Realm.init(this);
//
//    RealmConfiguration config = new RealmConfiguration.Builder()
//            .schemaVersion(1) // Increment when schema changes
//            .migration(new MyRealmMigration()) // Handle migration
//            .build();
//
//    Realm.setDefaultConfiguration(config);
//    realm = Realm.getDefaultInstance();
//}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
