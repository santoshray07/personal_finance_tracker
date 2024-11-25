package com.example.mr_me.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mr_me.R;
import com.example.mr_me.databinding.RowTransactionBinding;
import com.example.mr_me.models.Category;
import com.example.mr_me.models.Transaction;
import com.example.mr_me.utils.Constants;
import com.example.mr_me.utils.Helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

//import io.realm.RealmResults;

public class TransactionsAdapter  extends  RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder> {


    Context context;
//    RealmResults<Transaction> transactions;
    ArrayList<Transaction> transactions;

    public TransactionsAdapter(Context context, ArrayList<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.row_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        holder.binding.amount.setText(String.valueOf(transaction.getAmount()));
        holder.binding.transactionDate.setText(Helper.formatDate(transaction.getDate()));
        holder.binding.transactionCategory.setText(transaction.getCategory());
        holder.binding.accountLabel.setText(transaction.getType());

        Category transactionCategory = Constants.getCategoryDetails(transaction.getCategory());
//        assert transactionCategory != null;
        if (transactionCategory != null) {
            holder.binding.categoryIcon.setImageResource(transactionCategory.getCategoryImage());
        }
        if (transactionCategory != null) {
            holder.binding.categoryIcon.setBackgroundTintList(context.getColorStateList(transactionCategory.getCategoryColor()));
        }

        holder.binding.accountLabel.setBackgroundTintList(context.getColorStateList(Constants.getAccountsColor(transaction.getAccount())));


        if(transaction.getType().equals(Constants.INCOME)){
            holder.binding.amount.setTextColor(context.getColor(R.color.greenColor));
        } else if(transaction.getType().equals(Constants.EXPENSE)) {
            holder.binding.amount.setTextColor(context.getColor(R.color.redColor));
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {

        RowTransactionBinding binding;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowTransactionBinding.bind(itemView);
        }
    }
}
