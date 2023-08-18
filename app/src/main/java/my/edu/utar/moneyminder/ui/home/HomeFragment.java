package my.edu.utar.moneyminder.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import my.edu.utar.moneyminder.CashInActivity;
import my.edu.utar.moneyminder.CashOutActivity;
import my.edu.utar.moneyminder.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FloatingActionButton addFAB = binding.addFAB;
        FloatingActionButton removeFAB = binding.removeFAB;

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

    public void openCashInActivity() {
        Intent intent = new Intent(getContext(), CashInActivity.class);
        Toast.makeText(getContext(), "Woohoo~ Money in the bank!",
                Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    public void openCashOutActivity() {
        Intent intent = new Intent(getContext(), CashOutActivity.class);
        Toast.makeText(getContext(), "Budgeting is important, but so is enjoying life.",
                Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}