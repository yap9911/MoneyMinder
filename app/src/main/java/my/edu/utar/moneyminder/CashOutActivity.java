package my.edu.utar.moneyminder;

import android.app.DatePickerDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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

        // Define an array of category options
        String[] categoryOptions = {"Food & Beverage", "Transportation", "Rentals", "Bills", "Medicals",
        "Fun & Relax"};

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

                if (amount <= 0) {
                    Toast.makeText(CashOutActivity.this, "Why are you trying to add zero money?",
                            Toast.LENGTH_SHORT).show();
                } else {
                    validAmount = true;
                    Toast.makeText(CashOutActivity.this, "Budgeting is important, but so is enjoying life.",
                            Toast.LENGTH_SHORT).show();
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

    // Method to show the date picker dialog
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        // Create a DatePickerDialog with current date as default
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update the EditText with the selected date
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(year, month, dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        String selectedDate = dateFormat.format(selectedCalendar.getTime());
                        cashOutDateEditText.setText(selectedDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Show the date picker dialog
        datePickerDialog.show();
    }
}
