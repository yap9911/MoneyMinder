package my.edu.utar.moneyminder.ui.home;



import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;



import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;



import java.util.ArrayList;



import androidx.fragment.app.Fragment;
import my.edu.utar.moneyminder.CashInActivity;
import my.edu.utar.moneyminder.CashOutActivity;
import my.edu.utar.moneyminder.databinding.FragmentHomeBinding;



public class HomeFragment extends Fragment {



    private FragmentHomeBinding binding;
    private PieChart pieChart;
    private TextView balanceTextView;
    private TextView spendingTextView;
    private double totalBalance = 0.00;
    private double totalSpending = 0.00;



    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        balanceTextView = binding.balanceTextView;
        spendingTextView = binding.spendingTextView;
        pieChart = binding.pieChart;

        setupPieChart();

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

    private void addBalance(double amount) {
        totalBalance += amount;
        updateUI();
    }

    private void addSpending(double spendingAmount) {
        totalSpending += spendingAmount;
        totalBalance -= spendingAmount;
        if (totalBalance < 0) {
            totalBalance = 0;
        }
        updateUI();
    }

    private void setupPieChart() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) totalSpending, "Spending"));
        entries.add(new PieEntry((float) (totalBalance - totalSpending), "Balance"));

        PieDataSet dataSet = new PieDataSet(entries, "Budget Summary");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.invalidate(); // Refresh chart
    }



    private void updateUI() {
        balanceTextView.setText(String.format("Balance: $%.2f", totalBalance));
        spendingTextView.setText(String.format("Spending: $%.2f", totalSpending));

        setupPieChart();
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