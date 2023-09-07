package my.edu.utar.moneyminder.ui.transaction;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import my.edu.utar.moneyminder.EditCashInActivity;
import my.edu.utar.moneyminder.EditCashOutActivity;
import my.edu.utar.moneyminder.R;


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.transactionHolder>{

    private List<Transaction> transactionArrayList;
    private Activity activity;
    private View root;

    public CardAdapter(List<Transaction> transactionArrayList, Activity activity, View root) {
        this.transactionArrayList = transactionArrayList;
        this.activity = activity;
        this.root = root;
    }

    @Override
    public transactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item,
                parent, false);
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
        holder.noteTextView.setText("Note: " + transaction.getNote());


        // Set OnClickListener for the edit ImageButton
        holder.editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an editCashOutIntent to start the EditCashOutActivity
                Intent editCashOutIntent = new Intent(v.getContext(), EditCashOutActivity.class);
                Intent editCashInIntent = new Intent(v.getContext(), EditCashInActivity.class);

                // Get the data for the current transaction
                Transaction transaction = transactionArrayList.get(holder.getAdapterPosition());

                // Put extra data (id, amount, category, date and note) into the editCashOutIntent
                editCashOutIntent.putExtra("id", transaction.getId());
                editCashOutIntent.putExtra("amount", transaction.getAmount());
                editCashOutIntent.putExtra("category", transaction.getCategory());
                editCashOutIntent.putExtra("date", transaction.getDate());
                editCashOutIntent.putExtra("note", transaction.getNote());

                // Start the EditCashOutActivity with the Intent if it is a cash out transaction
                if (transaction.getCategory().equals("Cash In")) {
                    v.getContext().startActivity(editCashInIntent);
                    // Create an instance of the CardAdapter once, after retrieving all data
                    CardAdapter cardAdapter = new CardAdapter(transactionArrayList, activity, root);

                    // Find your RecyclerView by its ID
                    RecyclerView recyclerView = root.findViewById(R.id.transactionsRecyclerView);

                    // Create a LinearLayoutManager
                    LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
                    recyclerView.setLayoutManager(layoutManager);

                    // Set the CardAdapter as the adapter for your RecyclerView
                    recyclerView.setAdapter(cardAdapter);
                } else {
                    v.getContext().startActivity(editCashOutIntent);
                    // Create an instance of the CardAdapter once, after retrieving all data
                    CardAdapter cardAdapter = new CardAdapter(transactionArrayList, activity, root);

                    // Find your RecyclerView by its ID
                    RecyclerView recyclerView = root.findViewById(R.id.transactionsRecyclerView);

                    // Create a LinearLayoutManager
                    LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
                    recyclerView.setLayoutManager(layoutManager);

                    // Set the CardAdapter as the adapter for your RecyclerView
                    recyclerView.setAdapter(cardAdapter);
                }
            }
        });

        // Set OnClickListener for the delete ImageButton
        holder.deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click for the second ImageButton
                // You can perform actions or open dialogs, etc.
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionArrayList.size();
    }

    public class transactionHolder extends RecyclerView.ViewHolder {
        private TextView amountTextView, categoryTextView, dateTextView, noteTextView;
        private ImageButton editImageButton, deleteImageButton;

        public transactionHolder(View itemView) {
            super(itemView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            noteTextView = itemView.findViewById(R.id.noteTextView);
            editImageButton = itemView.findViewById(R.id.editImageButton);
            deleteImageButton = itemView.findViewById(R.id.deleteImageButton);
        }
    }

}
