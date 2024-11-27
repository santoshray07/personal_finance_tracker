package com.example.mr_me.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.mr_me.R;
import com.example.mr_me.databinding.FragmentStatsBinding;
import com.example.mr_me.models.Transaction;
import com.example.mr_me.utils.Constants;
import com.example.mr_me.utils.Helper;
import com.example.mr_me.viewmodels.MainViewModels;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.realm.RealmResults;

public class StatsFragment extends Fragment {
    public StatsFragment() {
        // Required empty public constructor
    }

    Calendar calendar;
    public MainViewModels viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentStatsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatsBinding.inflate(inflater);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModels.class);

        calendar = Calendar.getInstance();
        updateDate();

        binding.nextDateBtn.setOnClickListener(c-> {
            if(Constants.SELECTED_TAB_STATS == Constants.DAILY) {
                calendar.add(Calendar.DATE, 1);
            } else if(Constants.SELECTED_TAB_STATS == Constants.MONTHLY) {
                calendar.add(Calendar.MONTH, 1);
            }
            updateDate();
        });

        binding.prevDateBtn.setOnClickListener(c-> {
            if(Constants.SELECTED_TAB_STATS == Constants.DAILY) {
                calendar.add(Calendar.DATE, -1);
            } else if(Constants.SELECTED_TAB_STATS == Constants.MONTHLY) {
                calendar.add(Calendar.MONTH, -1);
            }
            updateDate();
        });


        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(Objects.equals(tab.getText(), "Monthly")) {
                    Constants.SELECTED_TAB_STATS = 1;
                    updateDate();
                } else if(Objects.equals(tab.getText(), "Daily")) {
                    Constants.SELECTED_TAB_STATS = 0;
                    updateDate();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Pie pie = AnyChart.pie();

        viewModel.categoriesTransactions.observe(getViewLifecycleOwner(), new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {

                if(!transactions.isEmpty()) {
                    binding.emptyState2.setVisibility(View.GONE);
                    binding.anyChat.setVisibility(View.VISIBLE);

                    List<DataEntry> data = new ArrayList<>();

                    Map<String, Double> categoryMap = new HashMap<>();

                    for(Transaction transaction : transactions) {
                        String category = transaction.getCategory();
                        double amount = transaction.getAmount();

                        if(categoryMap.containsKey(category)) {
                            double currentTotal = categoryMap.get(category).doubleValue();
                            currentTotal += Math.abs(amount);

                            categoryMap.put(category, currentTotal);
                        } else {
                            categoryMap.put(category, Math.abs(amount));
                        }
                    }

                    for(Map.Entry<String, Double> entry : categoryMap.entrySet()) {
                        data.add(new ValueDataEntry(entry.getKey(),entry.getValue()));
                    }
                    System.out.println(data);
                    pie.data(data);

                } else {
                    binding.emptyState2.setVisibility(View.VISIBLE);
                    binding.anyChat.setVisibility(View.GONE);
                }
            }
        });

        viewModel.getTransactions(calendar, Constants.SELECTED_STATS_TYPE);

        binding.anyChat.setChart(pie);

        return binding.getRoot();
    }
    void updateDate() {
        if(Constants.SELECTED_TAB_STATS == Constants.DAILY) {
            binding.currentDate.setText(Helper.formatDate(calendar.getTime()));
        } else if(Constants.SELECTED_TAB_STATS == Constants.MONTHLY) {
            binding.currentDate.setText(Helper.formatDateByMonth(calendar.getTime()));
        }
        viewModel.getTransactions(calendar, Constants.SELECTED_STATS_TYPE);
    }
}
