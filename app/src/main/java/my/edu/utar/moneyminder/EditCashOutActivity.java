package my.edu.utar.moneyminder;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import my.edu.utar.moneyminder.ui.transaction.Transaction;
import my.edu.utar.moneyminder.ui.transaction.TransactionFragment;

public class EditCashOutActivity extends AppCompatActivity {

    private EditText cashOutEditDateEditText;

    // Get the current date
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private String selectedDate = dateFormat.format(calendar.getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cash_out);

        // Retrieve the extra data (id, amount, category, date and note)
        Intent intent = getIntent();
        if (intent != null) {
            String documentId = intent.getStringExtra("id");
            String amount = intent.getStringExtra("amount");
            Double originalAmount = Double.parseDouble(amount);
            String category = intent.getStringExtra("category");
            String date = intent.getStringExtra("date");
            String note = intent.getStringExtra("note");
            int position = intent.getIntExtra("position", -1);

            EditText cashOutEditAmountEditText = findViewById(R.id.editCashOutAmountet);
            cashOutEditAmountEditText.setFilters(new InputFilter[]{new CashInActivity.DecimalDigitsInputFilter(2)});
            Spinner cashOutEditCategorySpinner = findViewById(R.id.editCashOutCategorySpinner);
            cashOutEditDateEditText = findViewById(R.id.editCashOutDateet);
            EditText cashOutEditNoteEditText = findViewById(R.id.editCashOutNoteet);

            cashOutEditAmountEditText.setText(amount);
            cashOutEditDateEditText.setText(date);
            cashOutEditNoteEditText.setText(note);

            // Define an array of category options
            String[] categoryOptions = {"Food & Beverage", "Transportation",
                    "Rentals", "Bills", "Medicals", "Fun & Relax"};

            // Create an ArrayAdapter for the Spinner
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, categoryOptions);

            // Specify the layout resource for the dropdown options
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Set the adapter for the Spinner
            cashOutEditCategorySpinner.setAdapter(spinnerAdapter);

            // Find the index of the retrieved category in the array or list
            int categoryIndex = -1;
            for (int i = 0; i < categoryOptions.length; i++) {
                if (categoryOptions[i].equals(category)) {
                    categoryIndex = i;
                    break;
                }
            }

            // Set the selected item in the Spinner
            if (categoryIndex != -1) {
                cashOutEditCategorySpinner.setSelection(categoryIndex);
            }

            cashOutEditDateEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDatePicker();
                }
            });

            Button UpdateButton = findViewById(R.id.UpdateButton);

            UpdateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    boolean validAmount = false;

                    String amountStr = cashOutEditAmountEditText.getText().toString();     // obtain the updated amount and convert to string
                    double updatedAmount;

                    try {
                        updatedAmount = Double.parseDouble(amountStr);      // convert updated amount into double
                    } catch (NumberFormatException e) {
                        Toast.makeText(EditCashOutActivity.this, "Invalid input. Please enter valid numbers for amount.",
                                Toast.LENGTH_SHORT).show();
                        return; // Exit the onClick method if parsing fails
                    }

                    if (updatedAmount <= 0) {  // input 0 in amount is not allowed
                        Toast.makeText(EditCashOutActivity.this, "Spending zero amount money in a bill?",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        validAmount = true; // amount is a valid input
                    }

                    if (validAmount) {

                        // Access a Cloud Firestore instance from your Activity
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        // Create a reference to the "Transactions" collection
                        CollectionReference transactionsRef = db.collection("Transactions");

                        // Get a reference to the specific document that want to update
                        DocumentReference docRef = transactionsRef.document(documentId);

                        // Create a Map to specify the field and its new value (replace "fieldToUpdate" and "newValue" with your actual field and value)
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("Amount", cashOutEditAmountEditText.getText().toString());
                        updates.put("Category", cashOutEditCategorySpinner.getSelectedItem().toString());
                        updates.put("Date", selectedDate);
                        updates.put("Note", cashOutEditNoteEditText.getText().toString());
                        updates.put("Last Updated Time", Timestamp.now());

                        Transaction transaction = new Transaction( documentId,
                                cashOutEditAmountEditText.getText().toString(),
                                cashOutEditCategorySpinner.getSelectedItem().toString(),
                                selectedDate,
                                cashOutEditNoteEditText.getText().toString()
                        );

                        // Update the document with the new field value
                        docRef.update(updates)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Document successfully updated
                                        Log.d(TAG, "Transaction updated successfully.");

                                        updateBalance(db, originalAmount, updatedAmount);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle errors
                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });

                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_back) {
            onBackPressed(); // Handle back arrow menu item click
            return true;
        }
        // Handle other menu item clicks if needed
        return super.onOptionsItemSelected(item);
    }

    // Method to show the date picker dialog and return the selected date as a String
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        // Create a DatePickerDialog with current date as default
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Get the selected date
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(year, month, dayOfMonth);

                        // Get the current date
                        Calendar currentDateCalendar = Calendar.getInstance();

                        // Compare the selected date with the current date
                        if (selectedCalendar.get(Calendar.YEAR) == currentDateCalendar.get(Calendar.YEAR) &&
                                selectedCalendar.get(Calendar.MONTH) == currentDateCalendar.get(Calendar.MONTH) &&
                                selectedCalendar.get(Calendar.DAY_OF_MONTH) == currentDateCalendar.get(Calendar.DAY_OF_MONTH)) {
                            // Selected date is today
                            selectedDate = dateFormat.format(selectedCalendar.getTime());
                            cashOutEditDateEditText.setText("Today");
                        } else {
                            // Calculate the difference in days
                            long diffInMillis = selectedCalendar.getTimeInMillis() - currentDateCalendar.getTimeInMillis();
                            long diffDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);

                            if (diffDays == 1) {
                                // Selected date is tomorrow;
                                selectedDate = dateFormat.format(selectedCalendar.getTime());
                                cashOutEditDateEditText.setText("Tomorrow");
                            } else if (diffDays == -1) {
                                // Selected date is yesterday
                                selectedDate = dateFormat.format(selectedCalendar.getTime());
                                cashOutEditDateEditText.setText("Yesterday");
                            } else {
                                // Not today, tomorrow, or yesterday, display the selected date
                                selectedDate = dateFormat.format(selectedCalendar.getTime());
                                cashOutEditDateEditText.setText(selectedDate);
                            }
                        }


                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Show the date picker dialog
        datePickerDialog.show();

    }

    // Function to update the balance in Firestore
    private void updateBalance(FirebaseFirestore db, double originalAmount, double updatedAmount) {
        // Get the current balance document
        DocumentReference balanceRef = db.collection("Balance").document("f8dT4dq1c74zpSwITBJR");

        // Update the balance by subtracting the original amount and adding the updated amount
        Map<String, Object> updates = new HashMap<>();
        updates.put("CashOutAmount", FieldValue.increment(-originalAmount + updatedAmount));

        balanceRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Balance updated successfully.");
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating balance", e);
                    }
                });
    }

}