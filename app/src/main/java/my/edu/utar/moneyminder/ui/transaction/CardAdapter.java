package my.edu.utar.moneyminder.ui.transaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import my.edu.utar.moneyminder.R;


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.transactionHolder>{

    private List<Transaction> transactionArrayList;

    public CardAdapter(List<Transaction> transactionArrayList) {
        this.transactionArrayList = transactionArrayList;
    }

    @Override
    public transactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(my.edu.utar.moneyminder.R.layout.transaction_item, parent, false);
        return new transactionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(transactionHolder holder, int position) {
        // Get the data for the current transaction
        Transaction transaction = transactionArrayList.get(position);

        // Bind data to the TextViews in the layout
        holder.amountTextView.setText("Amount: " + transaction.getAmount());
        holder.categoryTextView.setText("Category: " + transaction.getCategory());
        holder.dateTextView.setText("Date: " + transaction.getDate());
    }

    @Override
    public int getItemCount() {
        return transactionArrayList.size();
    }

    public class transactionHolder extends RecyclerView.ViewHolder {
        private TextView amountTextView, categoryTextView, dateTextView;

        public transactionHolder(View itemView) {
            super(itemView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}
