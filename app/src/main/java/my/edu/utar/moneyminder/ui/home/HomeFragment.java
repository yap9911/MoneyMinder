package my.edu.utar.moneyminder.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.checkerframework.checker.nullness.qual.NonNull;

import my.edu.utar.moneyminder.CashInActivity;
import my.edu.utar.moneyminder.CashOutActivity;
import my.edu.utar.moneyminder.Milestone;
import my.edu.utar.moneyminder.R;
import my.edu.utar.moneyminder.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Milestone milestone;
    private double currentAmount = 0.0;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FloatingActionButton addFAB = binding.addFAB;
        FloatingActionButton removeFAB = binding.removeFAB;
        milestone = new Milestone(0.0);
        FloatingActionButton setMilestoneButton = binding.setMilestoneButton;

        setMilestoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSetMilestoneDialog();
            }
        });

        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSetMilestoneDialog(); // Call the method here
            }
        });


        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCashInActivity();
            }
        });

        removeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCashOutActivity();
            }
        });

        return root;
    }




    private void showSetMilestoneDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Set Milestone Amount");

        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);

        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String amountStr = input.getText().toString();
                if (!amountStr.isEmpty()) {
                    double amount = Double.parseDouble(amountStr);
                    milestone.setTargetAmount(amount);
                    updateMilestoneTextView(); // Update the TextView here
                    checkMilestoneStatus();
                }
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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

    private void updateMilestoneTextView() {
        if (milestone != null) {
            TextView milestoneTextView = getView().findViewById(R.id.milestoneTextView);
            if (milestoneTextView != null) {
                milestoneTextView.setText("Milestone: $" + milestone.getTargetAmount());
            }
        }
    }

    private void checkMilestoneStatus () {
        if (milestone.isMilestoneReached(currentAmount)) {
            Toast.makeText(requireContext(), "Congratulations! You've reached 50% of your milestone.", Toast.LENGTH_SHORT).show();
        }
        if (milestone.isMilestoneFullyReached(currentAmount)) {
            Toast.makeText(requireContext(), "Congratulations! You've fully reached your milestone.", Toast.LENGTH_SHORT).show();
        }
    }

    }








