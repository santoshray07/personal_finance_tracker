package com.example.mr_me.views.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu; // Import added for Menu
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mr_me.R;
import com.example.mr_me.adapters.TransactionsAdapter;
import com.example.mr_me.databinding.ActivityMainBinding;
import com.example.mr_me.models.Transaction;
import com.example.mr_me.utils.Constants;
import com.example.mr_me.utils.Helper;
import com.example.mr_me.viewmodels.MainViewModels;
import com.example.mr_me.views.fragments.AddTransactionFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    Calendar calendar;
    public MainViewModels viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModels.class);

        binding.addTransactionBtn.setOnClickListener(c->{
            new AddTransactionFragment().show(getSupportFragmentManager(), null);
        });

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.orange));


        setSupportActionBar(binding.toolBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Transactions");

        Constants.setCategories();

        calendar = Calendar.getInstance();
        updateDate();

        binding.nextDateBtn.setOnClickListener(c->{
            calendar.add(Calendar.DATE, 1);
            updateDate();
            viewModel.getTransactions(calendar);
        });

        binding.prevDateBtn.setOnClickListener(c->{
            calendar.add(Calendar.DATE, -1);
            updateDate();
            viewModel.getTransactions(calendar);
        });


        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

binding.transactionsList.setLayoutManager(new LinearLayoutManager(this));
viewModel.transactions.observe(this, new Observer<RealmResults<Transaction>>() {
    @Override
    public void onChanged(RealmResults<Transaction> transactions) {
        TransactionsAdapter transactionsAdapter = new TransactionsAdapter(MainActivity.this, transactions);
        binding.transactionsList.setAdapter(transactionsAdapter);
        if(!transactions.isEmpty()){
            binding.emptyState.setVisibility(View.GONE);
        }else{
            binding.emptyState.setVisibility(View.VISIBLE);
        }

    }
});
viewModel.totalIncome.observe(this, new Observer<Double>() {
    @Override
    public void onChanged(Double aDouble) {
        binding.incomeLbl.setText(String.valueOf(aDouble));
    }
});

viewModel.totalExpense.observe(this, new Observer<Double>() {
    @Override
    public void onChanged(Double aDouble) {
        binding.expenseLbl.setText(String.valueOf(aDouble));
    }
});

viewModel.totalAmount.observe(this, new Observer<Double>() {
    @Override
    public void onChanged(Double aDouble) {
        binding.totalLbl.setText(String.valueOf(aDouble));
    }
});
viewModel.getTransactions(calendar);

    }


    public void getTransactions(){

    }

    void updateDate (){
        binding.currentDate.setText(Helper.formatDate(calendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
