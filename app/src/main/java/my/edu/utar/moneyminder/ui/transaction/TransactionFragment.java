package my.edu.utar.moneyminder.ui.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import my.edu.utar.moneyminder.databinding.FragmentTransactionBinding;

public class TransactionFragment extends Fragment {

    private FragmentTransactionBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTransactionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textTransaction;
        textView.setText("This is transaction fragment");
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}