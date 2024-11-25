package com.example.mr_me.views.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mr_me.R;
import com.example.mr_me.adapters.AccountsAdapter;
import com.example.mr_me.adapters.CategoryAdapter;
import com.example.mr_me.databinding.FragmentAddTransactionBinding;
import com.example.mr_me.databinding.ListDialogBinding;
import com.example.mr_me.models.Account;
import com.example.mr_me.models.Category;
import com.example.mr_me.utils.Constants;
import com.example.mr_me.utils.Helper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class AddTransactionFragment extends BottomSheetDialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentAddTransactionBinding binding;


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddTransactionBinding.inflate(inflater);
        binding.incomeBtn.setOnClickListener(c->{
            binding.incomeBtn.setBackground(requireContext().getDrawable(R.drawable.income_selector));
            binding.expenseBtn.setBackground(requireContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setTextColor(requireContext().getColor(R.color.textColor));
            binding.incomeBtn.setTextColor(requireContext().getColor(R.color.greenColor));
        });

        binding.expenseBtn.setOnClickListener(c->{
            binding.incomeBtn.setBackground(requireContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setBackground(requireContext().getDrawable(R.drawable.expense_selector));
            binding.expenseBtn.setTextColor(requireContext().getColor(R.color.redColor));
            binding.incomeBtn.setTextColor(requireContext().getColor(R.color.textColor));
        });

//        binding.selectDate.setOnClickListener(c->{
//            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext());
//            datePickerDialog.setOnDateSetListener((DatePicker, i,i1,i2)->{
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
//                calendar.set(Calendar.MONTH, datePicker.getMonth());
//                calendar.set(Calendar.YEAR, datePicker.getYear());
//            });
//            datePickerDialog.show();
//        });


        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDate = dateFormat1.format(calendar1.getTime());

// Set the default date in the TextInputLayout's EditText
        if (binding.selectDate.getEditableText() != null) {
            binding.selectDate.setText(currentDate);
        }


        binding.selectDate.setOnClickListener(c -> {
            // Get the current date
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Show DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, selectedYear, selectedMonth, selectedDay) -> {
                // Update the calendar with the selected date
                calendar.set(Calendar.YEAR, selectedYear);
                calendar.set(Calendar.MONTH, selectedMonth);
                calendar.set(Calendar.DAY_OF_MONTH, selectedDay);

                // Format the selected date
                String selectedDate = Helper.formatDate(calendar.getTime());

                // Set the formatted date as the text of the EditText inside the TextInputLayout
                if (binding.selectDate.getEditableText() != null) {
                    binding.selectDate.setText(selectedDate);
                }
            }, year, month, day);

            datePickerDialog.show();
        });

        binding.selectCategory.setOnClickListener(c->{
            ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog categoryDialog = new AlertDialog.Builder(getContext()).create();
            categoryDialog.setView(dialogBinding.getRoot());

            CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), Constants.categories, new CategoryAdapter.CategoryClickListener() {
                @Override
                public void onCategoryClicked(Category category) {
                    binding.selectCategory.setText(category.getCategoryName());
                    categoryDialog.dismiss();
                }
            });
            dialogBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
            dialogBinding.recyclerView.setAdapter(categoryAdapter);

            categoryDialog.show();
        });

        binding.selectAccountType.setOnClickListener(c->{
            ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog accountDialog = new AlertDialog.Builder(getContext()).create();
            accountDialog.setView(dialogBinding.getRoot());

            ArrayList<Account> accounts = new ArrayList<>();
            accounts.add(new Account(0,"Cash"));
            accounts.add(new Account(0,"Bank"));
            accounts.add(new Account(0,"PayTM"));
            accounts.add(new Account(0,"Gpay"));
            accounts.add(new Account(0,"Other"));

            AccountsAdapter adapter = new AccountsAdapter(getContext(), accounts, new AccountsAdapter.AccountsClickListener() {
                @Override
                public void onAccountSelected(Account account) {
                    binding.selectAccountType.setText(account.getAccountName());
                    accountDialog.dismiss();
                }
            });
            dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            dialogBinding.recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
            dialogBinding.recyclerView.setAdapter(adapter);
            accountDialog.show();

        });


        return binding.getRoot();
    }
}