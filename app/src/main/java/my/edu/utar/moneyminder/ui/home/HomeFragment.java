package my.edu.utar.moneyminder.ui.home;



import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;



import androidx.fragment.app.Fragment;

import org.checkerframework.checker.nullness.qual.NonNull;

import my.edu.utar.moneyminder.CashInActivity;
import my.edu.utar.moneyminder.CashOutActivity;
import my.edu.utar.moneyminder.databinding.FragmentHomeBinding;



public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private PieChart pieChart;
    private TextView balanceTextView;
    private TextView spendingTextView;
    private double totalCashIn;
    private double totalCashOut;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        balanceTextView = binding.balanceTextView;
        spendingTextView = binding.spendingTextView;
        pieChart = binding.pieChart;

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        getBalanceAndSpending(db);

        binding.addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCashInActivity();
            }
        });



        binding.removeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCashOutActivity();
            }
        });

        return root;
    }


    private void setupPieChart() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) totalCashOut, "Spending"));
        entries.add(new PieEntry((float) (totalCashIn - totalCashOut), "Balance"));

        PieDataSet dataSet = new PieDataSet(entries, "Budget Summary");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(16f);
        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.invalidate(); // Refresh chart
    }


    private void getBalanceAndSpending(FirebaseFirestore db){
        // Assuming you have a DocumentReference for the document you want to retrieve
        DocumentReference balanceRef = db.collection("Balance").document("f8dT4dq1c74zpSwITBJR");

        balanceRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Document exists, you can access its data
                            double CashInAmount = documentSnapshot.getDouble("CashInAmount");
                            double CashOutAmount = documentSnapshot.getDouble("CashOutAmount");

                            totalCashIn = CashInAmount;
                            totalCashOut= CashOutAmount;

                            balanceTextView.setText("Total cash in: " + String.valueOf(totalCashIn));
                            spendingTextView.setText("Total cash out: " + String.valueOf(totalCashOut));
                            setupPieChart();

                            // Do something with these values
                        } else {
                            // Document does not exist
                            Log.d(TAG, "Document does not exist");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors
                        Log.w(TAG, "Error reading document", e);
                    }
                });
    }

    public void openCashInActivity() {
        Intent intent = new Intent(getContext(), CashInActivity.class);
        startActivity(intent);
    }

    public void openCashOutActivity() {
        Intent intent = new Intent(getContext(), CashOutActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}