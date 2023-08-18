package my.edu.utar.moneyminder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CashOutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_out);

        TextView CashOutBalancetv;
        CashOutBalancetv = findViewById(R.id.CashOutBalancetv);
        CashOutBalancetv.setText("Total balance: ");

        EditText CashOutAmountet = findViewById(R.id.CashOutAmountet);
        CashOutAmountet.setFilters(new InputFilter[]{new CashInActivity.DecimalDigitsInputFilter(2)});

        Button CashOutAddButton = findViewById(R.id.CashOutAddButton);

        CashOutAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean validAmount = false;

                String amountStr = CashOutAmountet.getText().toString();          // obtain the amount and convert to string
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
}