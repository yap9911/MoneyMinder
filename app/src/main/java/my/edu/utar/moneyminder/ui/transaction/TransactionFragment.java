package my.edu.utar.moneyminder.ui.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;

import my.edu.utar.moneyminder.databinding.FragmentTransactionBinding;
import my.edu.utar.moneyminder.databinding.FragmentTransactionBinding;

public class TransactionFragment extends Fragment {

    private TransactionViewModel transactionViewModel;
    private FragmentTransactionBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        transactionViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(TransactionViewModel.class);

        binding = FragmentTransactionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textTransaction;
        transactionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}