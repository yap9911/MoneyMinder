package my.edu.utar.moneyminder.ui.VirtualAdvisor;

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

import my.edu.utar.moneyminder.databinding.FragmentVirtualAdvisorBinding;

public class VirtualAdvisorFragment extends Fragment {

    private VirtualAdvisorViewModel virtualAdvisorViewModel;
    private FragmentVirtualAdvisorBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        virtualAdvisorViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(VirtualAdvisorViewModel.class);

        binding = FragmentVirtualAdvisorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textVirtualAdvisor;
        virtualAdvisorViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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