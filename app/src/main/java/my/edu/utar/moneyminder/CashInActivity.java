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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.Timestamp;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;


public class CashInActivity extends AppCompatActivity {

    private EditText CashInAmountEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_in);


        TextView cashInBalanceTextView = findViewById(R.id.CashInBalancetv);
        // Display the current balance of the user
        cashInBalanceTextView.setText("Total balance: ");

        CashInAmountEditText = findViewById(R.id.CashInAmountet);
        // Set the maximum decimal place for the edit text to 2
        CashInAmountEditText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});

        Button CashInAddButton = findViewById(R.id.CashInAddButton);

        CashInAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean validAmount = false;

                String amountStr = CashInAmountEditText.getText().toString();          // obtain the amount and convert to string
                double amount;

                try {
                    amount = Double.parseDouble(amountStr);      // convert amount into integer
                } catch (NumberFormatException e) {
                    Toast.makeText(CashInActivity.this, "Invalid input. Please enter valid numbers for amount.",
                            Toast.LENGTH_SHORT).show();
                    return; // Exit the onClick method if parsing fails
                }

                if (amount <= 0) {
                    Toast.makeText(CashInActivity.this, "Why are you trying to add zero money?",
                            Toast.LENGTH_SHORT).show();
                } else {
                    validAmount = true;
                    Toast.makeText(CashInActivity.this, "Woohoo~ Money in the bank!",
                            Toast.LENGTH_SHORT).show();
                    // Access a Cloud Firestore instance from your Activity

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Create a new user with a first and last name
                    Map<String, Object> CashInTrans = new HashMap<>();
                    CashInTrans.put("Amount", CashInAmountEditText.getText().toString());
                    CashInTrans.put("Category", "Cash in");
                    CashInTrans.put("Date", Timestamp.now());

                    // Add a new document with a generated ID
                    db.collection("Transactions")
                            .add(CashInTrans)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
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

