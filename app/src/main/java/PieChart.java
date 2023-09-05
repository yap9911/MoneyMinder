package my.edu.utar.moneyminder;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView balanceTextView;
    private TextView spendingTextView;
    private double totalBalance = 0.00;
    private double SpendingAmount = 0.00;
    private DatabaseReference transactionsRef;
    private PieChart pieChart;
    private DatabaseReference mDatabaseReference;
    private ArrayList<PieEntry> pieEntryList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        balanceTextView = findViewById(R.id.balanceTextView);
        spendingTextView = findViewById(R.id.SpendingTextView);
        pieChart = findViewById(R.id.pieChart);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        numbersCollection = db.collection("Balance");

        fetchDataFromFirestore();
        fetchTotalBalanceFromFirebase();
        fetchDataFromFirebase();
        calculateTotalSpendingFromFirebase();

        setupChart();
        setValues();
    }
    private void fetchBalanceFromFirestore() {  // New Method
        numbersCollection.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Double fetchedBalance = document.getDouble("balance");
                                if (fetchedBalance != null) {
                                    totalBalance = fetchedBalance;
                                    setValues();
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting balance.", task.getException());
                        }
                    }
                });
    }

    private void calculateTotalSpendingFromFirebase() {
        transactionsRef = FirebaseDatabase.getInstance().getReference("transactions");

        transactionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                double totalAmount = 0.0;

                for (DataSnapshot transactionSnapshot : dataSnapshot.getChildren()) {
                    Double amount = transactionSnapshot.child("amount").getValue(Double.class);
                    if (amount != null) {
                        totalAmount += amount;
                    }
                }

                spendingTextView.setText("Total Spending: $" + totalAmount);
                setValues();
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
                Log.e("MainActivity", "Error reading from database", databaseError.toException());
            }
        });
    }

    private void fetchDataFromFirestore() {
        db.collection("Transactions").document("4FVdXGgEHp27IT8zc3WW")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    private void setupChart() {
        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "Categories");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        pieDataSet.setValueTextColor(getResources().getColor(R.color.white));
        pieData.setValueTextSize(12f);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void setValues() {
        double remainingBalance = totalBalance - SpendingAmount;
        pieEntryList.clear(); // Clear old entries before adding new ones
        pieEntryList.add(new PieEntry((float) SpendingAmount, "Expenses"));
        pieEntryList.add(new PieEntry((float) remainingBalance, "Remaining Balance"));
        balanceTextView.setText(String.format(Locale.getDefault(), "%.2f", totalBalance));
        spendingTextView.setText(String.format(Locale.getDefault(), "%.2f", SpendingAmount));
    }

    private void fetchTotalBalanceFromFirebase() {
        DatabaseReference balanceRef = FirebaseDatabase.getInstance().getReference("total_balance");

        balanceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                Double fetchedBalance = dataSnapshot.getValue(Double.class);
                if (fetchedBalance != null) {
                    totalBalance = fetchedBalance;
                    setValues();
                }
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
                balanceTextView.setText("Error fetching balance!");
            }
        });
    }

    private void fetchDataFromFirebase() {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                Double fetchedBalance = dataSnapshot.child("totalBalance").getValue(Double.class);
                Double fetchedSpending = dataSnapshot.child("SpendingAmount").getValue(Double.class);

                if (fetchedBalance != null) totalBalance = fetchedBalance;
                if (fetchedSpending != null) SpendingAmount = fetchedSpending;

                setValues();
            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
                Log.e("MainActivity", "Error reading from database", databaseError.toException());
            }
        });
    }
}
