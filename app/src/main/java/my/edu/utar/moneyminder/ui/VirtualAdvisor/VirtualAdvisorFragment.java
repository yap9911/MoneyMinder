package my.edu.utar.moneyminder.ui.VirtualAdvisor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.checkerframework.checker.nullness.qual.NonNull;

import my.edu.utar.moneyminder.databinding.FragmentVirtualAdvisorBinding;

public class VirtualAdvisorFragment extends Fragment {

    private FragmentVirtualAdvisorBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentVirtualAdvisorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textVirtualAdvisor;
        textView.setText("This is VirtualAdvisor fragment");
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}