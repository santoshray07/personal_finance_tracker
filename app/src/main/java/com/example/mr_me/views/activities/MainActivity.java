package com.example.mr_me.views.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu; // Import added for Menu
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.mr_me.views.fragments.StatsFragment;
import com.example.mr_me.views.fragments.TransactionsFragment;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

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
    int selectedTab = 0;

    Calendar calendar;
    public MainViewModels viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModels.class);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.orange));


        setSupportActionBar(binding.toolBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Transactions");

        @SuppressLint("CommitTransaction") FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new TransactionsFragment());
        transaction.commit();

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                @SuppressLint("CommitTransaction") FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(item.getItemId()==R.id.transitions){
                    transaction.replace(R.id.content, new TransactionsFragment());
                    getSupportFragmentManager().popBackStack();
                } else if(item.getItemId()==R.id.stats){
                    transaction.replace(R.id.content, new StatsFragment());
                    transaction.addToBackStack(null);

                }
                transaction.commit();
                return true;
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Constants.setCategories();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
