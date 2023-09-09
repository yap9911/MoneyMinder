package my.edu.utar.moneyminder.ui.transaction;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import my.edu.utar.moneyminder.EditCashInActivity;
import my.edu.utar.moneyminder.EditCashOutActivity;
import my.edu.utar.moneyminder.R;


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.transactionHolder> {

    private List<Transaction> transactionArrayList;
    private Activity activity;

    public CardAdapter(List<Transaction> transactionArrayList, Activity activity) {
        this.transactionArrayList = transactionArrayList;
        this.activity = activity;
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
                // Get the data for the current transaction
                Transaction transaction = transactionArrayList.get(holder.getAdapterPosition());

                // Create an intent based on the category
                Intent editIntent;
                if (transaction.getCategory().equals("Cash in")) {
                    editIntent = new Intent(v.getContext(), EditCashInActivity.class);
                } else {
                    editIntent = new Intent(v.getContext(), EditCashOutActivity.class);
                }

                // Put the transaction data into the intent
                editIntent.putExtra("id", transaction.getId());
                editIntent.putExtra("amount", transaction.getAmount());
                editIntent.putExtra("category", transaction.getCategory());
                editIntent.putExtra("date", transaction.getDate());
                editIntent.putExtra("note", transaction.getNote());;

                // Start the appropriate edit activity
                v.getContext().startActivity(editIntent);

            }
        });

        // Set OnClickListener for the delete ImageButton
        holder.deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click for the second ImageButton
                PopUpWindow(transaction, holder);
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

    // Pop-up window for delete button
    public void PopUpWindow(Transaction transaction, transactionHolder holder){

        LayoutInflater inflater = activity.getLayoutInflater();
        // Inflate the popup layout
        View popupView = inflater.inflate(R.layout.popupwindowlayout, null);

        // Create a PopupWindow
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // Find the buttons within the popup layout
        Button btnYes = popupView.findViewById(R.id.btnYes);
        Button btnNo = popupView.findViewById(R.id.btnNo);

        // Set click listeners for the buttons
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "Yes" button click

                // Get a reference to the Firestore transaction document
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("Transactions").document(transaction.getId());

                // Get the current balance document
                DocumentReference balanceRef = db.collection("Balance").document("f8dT4dq1c74zpSwITBJR");

                // Update the balance by subtracting the original amount and adding the updated amount
                Map<String, Object> updates = new HashMap<>();

                if(transaction.getCategory().equals("Cash in")) {
                    updates.put("CashInAmount", FieldValue.increment(-Double.parseDouble(transaction.getAmount())));
                }
                else {
                    updates.put("CashOutAmount", FieldValue.increment(-Double.parseDouble(transaction.getAmount())));
                }

                // Delete the document from Firestore
                docRef.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Document successfully deleted
                                // Remove the transaction from your local data source
                                transactionArrayList.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle errors
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });

                balanceRef.update(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Balance updated successfully.");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating balance", e);
                            }
                        });
                popupWindow.dismiss();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "No" button click
                popupWindow.dismiss();
            }
        });
        // Show the popup window centered on the screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }

}
