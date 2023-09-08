package my.edu.utar.moneyminder.ui.VirtualAdvisor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import org.checkerframework.checker.nullness.qual.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import my.edu.utar.moneyminder.R;
import my.edu.utar.moneyminder.databinding.FragmentVirtualAdvisorBinding;

public class VirtualAdvisorFragment extends Fragment {

    private FragmentVirtualAdvisorBinding binding;

    // Initialize Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentVirtualAdvisorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize the TextView
        TextView tv1 = root.findViewById(R.id.tv1);
        TextView tv2 = root.findViewById(R.id.tv2);
        TextView tv3 = root.findViewById(R.id.tv3);

        // Initialize the Button
        Button button1 = root.findViewById(R.id.btn1);
        Button button2 = root.findViewById(R.id.btn2);
        Button button3 = root.findViewById(R.id.btn3);

        // Reference to the Firestore collection
        CollectionReference numbersCollection = db.collection("Balance");

        // Query the collection to get the latest document based on a timestamp field
        Query latestQuery = numbersCollection.orderBy("timestamp", Query.Direction.DESCENDING).limit(1);

        // Query the collection for a document (you may need more specific criteria here)
        latestQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    // Assuming you want to display the first document's "number" field
                    DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                    if (document != null) {
                        Long number = document.getLong("amount");
                        if (number != null) {
                            // Set the number in the TextView
                            tv1.setText("Total Balance: RM" + String.valueOf(number) +"\nEssentials: 40%\nWants: 20%\nSavings: 40%");
                            tv2.setText("Total Balance: RM" + String.valueOf(number) +"\nEssentials: 50%\nWants: 20%\nSavings: 30%");
                            tv3.setText("Total Balance: RM" + String.valueOf(number) +"\nEssentials: 60%\nWants: 25%\nSavings: 15%");

                            button1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showAlert("Budget Saving Plan 1: The Aggressive Saver", "Total Balance: RM" + String.valueOf(number) +
                                            "\n\nEssentials (40%): RM" + String.valueOf(number*40/100) + "\nCategory: Housing, Utilities, Groceries, Transportation, Bill" +
                                            "\n\nWants (20%): RM" + String.valueOf(number*20/100) + "\nCategory: Food & Beverage, Fun, Hobby, Shopping" +
                                            "\n\nSavings & Debt Repayment (40%): RM" + String.valueOf(number*40/100) + "\nCategory: Emergency Fund, Retirement Contributions, Aggressive Debt Repayment, Medicine");
                                }
                            });

                            button2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showAlert("Budget Saving Plan 2: The Balanced Saver", "Total Balance: RM" + String.valueOf(number) +
                                            "\n\nEssentials (50%): RM" + String.valueOf(number*50/100) + "\nCategory: Housing, Utilities, Groceries, Transportation, Bill" +"\n" +
                                            "\n\nWants (20%): RM" + String.valueOf(number*20/100) + "\nCategory: Food & Beverage, Fun, Hobby, Shopping"  +
                                            "\n\nSavings & Debt Repayment (30%): RM" + String.valueOf(number*30/100) + "\nCategory: Emergency Fund, Retirement Contributions, Debt Repayment, Medicine");
                                }
                            });

                            button3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showAlert("Budget Saving Plan 3: The Lifestyle Focused", "Total Balance: RM" + String.valueOf(number)  +
                                            "\n\nEssentials (60%): RM" + String.valueOf(number*60/100) + "\nCategory: Housing, Utilities, Groceries, Transportation, Bill" +"\n" +
                                            "\n\nWants (25%): RM" + String.valueOf(number*25/100) + "\nCategory: Food & Beverage, Fun, Hobby, Shopping"  +
                                            "\n\nSavings & Debt Repayment (15%): RM" + String.valueOf(number*15/100) + "\nCategory: Emergency Fund, Retirement Contributions, Debt Repayment, Medicine");
                                }
                            });
                        }
                    }
                }
            }
        });

        return root;
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}