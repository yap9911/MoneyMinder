package my.edu.utar.moneyminder.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import my.edu.utar.moneyminder.MainActivity;
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

            }
        });

        removeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return root;
    }

//    public void openCashInActivity() {
//        Intent intent = new Intent(getContext(), CashInActivity.class);
//        startActivity(intent);
//    }
//
//    public void openCashOutActivity() {
//        Intent intent = new Intent(getContext(), CashOutActivity.class);
//        startActivity(intent);
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}