package my.edu.utar.moneyminder;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.Timestamp;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;


public class CashInActivity extends AppCompatActivity {

    private EditText cashInAmountEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_in);


        TextView cashInBalanceTextView = findViewById(R.id.CashInBalancetv);
        // Display the current balance of the user
        cashInBalanceTextView.setText("Total balance: ");

        cashInAmountEditText = findViewById(R.id.CashInAmountet);
        // Set the maximum decimal place for the edit text to 2
        cashInAmountEditText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});

        EditText cashInNoteEditText = findViewById(R.id.CashInNoteet);

        Button CashInAddButton = findViewById(R.id.CashInAddButton);

        CashInAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean validAmount = false;

                String amountStr = cashInAmountEditText.getText().toString();          // obtain the amount and convert to string
                double amount;

                try {
                    amount = Double.parseDouble(amountStr);      // convert amount into integer
                } catch (NumberFormatException e) {
                    Toast.makeText(CashInActivity.this, "Invalid input. Please enter valid numbers for amount.",
                            Toast.LENGTH_SHORT).show();
                    return; // Exit the onClick method if parsing fails
                }

                if (amount <= 0) {  // input 0 in amount is not allowed
                    Toast.makeText(CashInActivity.this, "Why are you trying to add zero amount of money?",
                            Toast.LENGTH_SHORT).show();
                } else {
                    validAmount = true;     // amount is a valid input
                }

                if (validAmount){

                    // Access a Cloud Firestore instance from your Activity

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Create a new transaction with a amount, category, date
                    Map<String, Object> CashInTrans = new HashMap<>();
                    CashInTrans.put("Amount", cashInAmountEditText.getText().toString());
                    CashInTrans.put("Category", "Cash in");
                    CashInTrans.put("Date", Timestamp.now());
                    CashInTrans.put("Note", cashInNoteEditText.getText().toString());

                    // Add a new document with a generated ID
                    db.collection("Transactions")
                            .add(CashInTrans)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    // Add the amount to the balance
                                    increaseBalance(db, amount);
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

    // Function to update the balance in Firestore
    private void increaseBalance(FirebaseFirestore db, double amount) {
        // Get the current balance document
        DocumentReference balanceRef = db.collection("Balance").document("JtzWpeI9bBUdOwOO4oWP");

        // Update the balance by adding the transaction amount
        balanceRef.update("amount", FieldValue.increment(amount))
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

    // Method to return to Main Activity
    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    /**
     * Input filter that limits the number of decimal digits that are allowed to be
     * entered.
     */
    public static class DecimalDigitsInputFilter implements InputFilter {

        private final int decimalDigits;

        /**
         * //Constructor.
         *
         * @param decimalDigits maximum decimal digits
         */
        public DecimalDigitsInputFilter(int decimalDigits) {
            this.decimalDigits = decimalDigits;
        }

        @Override
        public CharSequence filter(CharSequence source, // new text user tries to input
                                   int start,           // starting index of "source" text
                                   int end,             // ending index of "source" text
                                   Spanned dest,        // current text before user's input
                                   int dstart,          // starting index of "dest" text
                                   int dend) {          // ending index of "dest" text


            int dotPos = -1; // the position of the dot, "."
            int len = dest.length();
            for (int i = 0; i < len; i++) {
                char c = dest.charAt(i);
                if (c == '.' || c == ',') {
                    dotPos = i; // get the position of the dot and break
                    break;
                }
            }
            if (dotPos >= 0) { // if user enter a dot

                // protects against many dots
                if (source.equals(".") || source.equals(",")) {
                    return "";
                }
                // if the text is entered before the dot
                if (dend <= dotPos) {
                    return null;
                }
                if (len - dotPos > decimalDigits) {
                    return "";
                }
            }

            return null;
        }
    }
}

