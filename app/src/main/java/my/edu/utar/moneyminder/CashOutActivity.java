package my.edu.utar.moneyminder;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
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

public class CashOutActivity extends AppCompatActivity {

    private EditText cashOutDateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_out);

        TextView CashOutBalanceTextView = findViewById(R.id.CashOutBalancetv);
        // Display the current balance of the user
        CashOutBalanceTextView.setText("Total balance: ");

        EditText CashOutAmountEditText = findViewById(R.id.CashOutAmountet);
        // Set the maximum decimal place for the edit text to 2
        CashOutAmountEditText.setFilters(new InputFilter[]{new CashInActivity.DecimalDigitsInputFilter(2)});

        Spinner cashOutCategorySpinner = findViewById(R.id.CashOutCategorySpinner);
        EditText cashOutNoteEditText = findViewById(R.id.CashOutNoteet);

        // Define an array of category options
        String[] categoryOptions = {"Food & Beverage", "Transportation",
                "Rentals", "Bills", "Medicals", "Fun & Relax"};

        // Create an ArrayAdapter for the Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categoryOptions);

        // Specify the layout resource for the dropdown options
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter for the Spinner
        cashOutCategorySpinner.setAdapter(spinnerAdapter);

        // Find the EditText for the date
        cashOutDateEditText = findViewById(R.id.CashOutDateet);

        // Set an OnClickListener for the date EditText
        cashOutDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the date picker dialog
                showDatePicker();
            }
        });

        Button CashOutAddButton = findViewById(R.id.CashOutAddButton);

        CashOutAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean validAmount = false;

                String amountStr = CashOutAmountEditText.getText().toString();          // obtain the amount and convert to string
                double amount;

                try {
                    amount = Double.parseDouble(amountStr);      // convert amount into integer
                } catch (NumberFormatException e) {
                    Toast.makeText(CashOutActivity.this, "Invalid input. Please enter valid numbers for amount.",
                            Toast.LENGTH_SHORT).show();
                    return; // Exit the onClick method if parsing fails
                }

                if (amount <= 0) {  // input 0 in amount is not allowed
                    Toast.makeText(CashOutActivity.this, "Spending zero amount money in a bill?",
                            Toast.LENGTH_SHORT).show();
                }

                else {
                    validAmount = true; // amount is a valid input
                }

                if (validAmount){

                    // Access a Cloud Firestore instance from your Activity

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Create a new transaction with a amount, category, date
                    Map<String, Object> CashOutTrans = new HashMap<>();
                    CashOutTrans.put("Amount", CashOutAmountEditText.getText().toString());
                    CashOutTrans.put("Category", cashOutCategorySpinner.getSelectedItem().toString());
                    CashOutTrans.put("Date", Timestamp.now());
                    CashOutTrans.put("Note", cashOutNoteEditText.getText().toString());

                    // Add a new document with a generated ID
                    db.collection("Transactions")
                            .add(CashOutTrans)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    reduceBalance(db, amount);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                    openMainActivity();
                }
            }
        });
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

    // Method to return to Main Activity
    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Method to show the date picker dialog
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
                            cashOutDateEditText.setText("Today");
                        } else {
                            // Calculate the difference in days
                            long diffInMillis = selectedCalendar.getTimeInMillis() - currentDateCalendar.getTimeInMillis();
                            long diffDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);

                            if (diffDays == 1) {
                                // Selected date is tomorrow
                                cashOutDateEditText.setText("Tomorrow");
                            } else if (diffDays == -1) {
                                // Selected date is yesterday
                                cashOutDateEditText.setText("Yesterday");
                            } else {
                                // Not today, tomorrow, or yesterday, display the selected date
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                String selectedDate = dateFormat.format(selectedCalendar.getTime());
                                cashOutDateEditText.setText(selectedDate);
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
    private void reduceBalance(FirebaseFirestore db, double amount) {
        // Get the current balance document
        DocumentReference balanceRef = db.collection("Balance").document("JtzWpeI9bBUdOwOO4oWP");

        // Update the balance by adding the transaction amount
        balanceRef.update("amount", FieldValue.increment(-amount))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Balance updated successfully.");
                        openMainActivity();
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
