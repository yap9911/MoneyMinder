package my.edu.utar.moneyminder.ui.transaction;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import my.edu.utar.moneyminder.R;
import my.edu.utar.moneyminder.databinding.FragmentTransactionBinding;

public class TransactionFragment extends Fragment {

    private FragmentTransactionBinding binding;
    private TextView DateSelected;
    private TextView categorySelected;
    private Calendar startDateCalendar;
    private Calendar endDateCalendar;

    private String dateString = "Date: Today";          // date String that is used to filter the transactions based on date
                                                        // set to "Today" by default
    private String categoryString = "Category: All";    // category String that is used to filter the transactions based on category
                                                        // set to "All" by default

    // Get the current date
    private Calendar calendar = Calendar.getInstance();

    private Date startDate = calendar.getTime();        // startDate and endDate String that is used
    private Date endDate = calendar.getTime();          // to filter the transactions based on date
                                                        // both are set to today as default

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTransactionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DateSelected = binding.timeRange;
        categorySelected = binding.Category;
        ImageButton filterButton = binding.filterButton;

        // Set a click listener on the ImageButton
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a PopupMenu
                PopupMenu popupMenu = new PopupMenu(getContext(), filterButton);

                // Inflate your transactions_option_menu
                popupMenu.getMenuInflater().inflate(R.menu.transactions_options_menu, popupMenu.getMenu());

                // Set an item click listener for the menu items
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.DayOption:

                                // Handle DayOption click

                                // Display the current date in a Toast
                                Toast.makeText(getContext(), "Displaying today transaction ",
                                        Toast.LENGTH_SHORT).show();

                                DateSelected.setText("Date: Today");

                                dateString = DateSelected.getText().toString();
                                categoryString = categorySelected.getText().toString();
                                dateAndCategoryFilter(dateString, categoryString, root);
                                break;

                            case R.id.WeekOption:

                                // Handle WeekOption click

                                Toast.makeText(getContext(), "Displaying this week's transaction", Toast.LENGTH_SHORT).show();

                                DateSelected.setText("Date: This week");

                                dateString = DateSelected.getText().toString();
                                categoryString = categorySelected.getText().toString();
                                dateAndCategoryFilter(dateString, categoryString, root);

                                break;

                            case R.id.MonthOption:
                                // Handle MonthOption click


                                // Get the current month from the Calendar
                                int currentMonth = calendar.get(Calendar.MONTH) + 1; // Adding 1 because months are 0-based

                                // Display the current month in a Toast
                                String monthString = getMonthString(currentMonth);
                                Toast.makeText(getContext(), "Displaying " + monthString + "'s transaction", Toast.LENGTH_SHORT).show();

                                DateSelected.setText("Date: " + monthString);

                                dateString = DateSelected.getText().toString();
                                categoryString = categorySelected.getText().toString();
                                dateAndCategoryFilter(dateString, categoryString, root);

                                break;

                            case R.id.CustomOption:
                                // Handle CustomOption click
                                startDateCalendar = Calendar.getInstance();
                                endDateCalendar = Calendar.getInstance();

                                showDatePickerDialog(new DateSelectionCallback() {
                                    @Override
                                    public void onDateSelected(String dateString) {
                                        // The callback will be invoked after the user selects a date.
                                        categoryString = categorySelected.getText().toString();
                                        dateAndCategoryFilter(dateString, categoryString, root);
                                    }
                                });
                                break;


                            case R.id.AllOption:
                                // Handle FNBOption click

                                categorySelected.setText("Category: All");

                                dateString = DateSelected.getText().toString();
                                categoryString = categorySelected.getText().toString();

                                dateAndCategoryFilter(dateString, categoryString, root);
                                break;

                            case R.id.FNBOption:
                                // Handle FNBOption click

                                categorySelected.setText("Category: Food & Beverage");

                                dateString = DateSelected.getText().toString();
                                categoryString = categorySelected.getText().toString();

                                dateAndCategoryFilter(dateString, categoryString, root);
                                break;

                            case R.id.TransportOption:
                                // Handle TransportOption click

                                categorySelected.setText("Category: Transportation");

                                dateString = DateSelected.getText().toString();
                                categoryString = categorySelected.getText().toString();

                                dateAndCategoryFilter(dateString, categoryString, root);
                                break;

                            case R.id.RentalsOption:
                                // Handle Rentalsoption click

                                categorySelected.setText("Category: Rentals");

                                dateString = DateSelected.getText().toString();
                                categoryString = categorySelected.getText().toString();

                                dateAndCategoryFilter(dateString, categoryString, root);
                                break;

                            case R.id.BillOption:
                                // Handle BillOption click

                                categorySelected.setText("Category: Bills");

                                dateString = DateSelected.getText().toString();
                                categoryString = categorySelected.getText().toString();

                                dateAndCategoryFilter(dateString, categoryString, root);
                                break;

                            case R.id.MedicalOption:
                                // Handle MedicalOption click

                                categorySelected.setText("Category: Medicals");

                                dateString = DateSelected.getText().toString();
                                categoryString = categorySelected.getText().toString();

                                dateAndCategoryFilter(dateString, categoryString, root);
                                break;

                            case R.id.FunNRelaxOption:
                                // Handle MedicalOption click

                                categorySelected.setText("Category: Fun & Relax");

                                dateString = DateSelected.getText().toString();
                                categoryString = categorySelected.getText().toString();

                                dateAndCategoryFilter(dateString, categoryString, root);
                                break;

                            case R.id.CashInOption:
                                // Handle CashInOption click

                                categorySelected.setText("Category: Cash in");

                                dateString = DateSelected.getText().toString();
                                categoryString = categorySelected.getText().toString();

                                dateAndCategoryFilter(dateString, categoryString, root);
                                break;

                            default:
                                break;
                        }
                        return false;
                    }
                });
                // Show the PopupMenu
                popupMenu.show();
            }
        });
        dateAndCategoryFilter(dateString, categoryString, root );
        return root;
    }

    // A method that return the month based on int month in the parameter
    private String getMonthString(int month) {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        if (month >= 1 && month <= 12) {
            return months[month - 1];
        }
        return "Invalid Month";
    }

    // A method to check whether the input is a month
    public boolean isMonth(String input) {
        // Define an array of month names
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

        // Check if the input string is a month name
        for (String month : monthNames) {
            if (input.equalsIgnoreCase(month)) {
                return true;
            }
        }

        // If the input is not a month name, return false
        return false;
    }

    public interface DateSelectionCallback {
        void onDateSelected(String dateString);
    }

    // A method to display 2 datepicker so that user can select
    // start date and end date for filter purpose
    private void showDatePickerDialog(DateSelectionCallback callback) {
        // Get the current date for the DatePickerDialog
        Calendar currentDateCalendar = Calendar.getInstance();

        // Create a DatePickerDialog for the start date
        DatePickerDialog startDatePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        startDateCalendar.set(Calendar.YEAR, year);
                        startDateCalendar.set(Calendar.MONTH, month);
                        startDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateTextView();
                        callback.onDateSelected(DateSelected.getText().toString());
                    }
                },
                currentDateCalendar.get(Calendar.YEAR),
                currentDateCalendar.get(Calendar.MONTH),
                currentDateCalendar.get(Calendar.DAY_OF_MONTH)
        );

        // Create a DatePickerDialog for the end date
        DatePickerDialog endDatePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        endDateCalendar.set(Calendar.YEAR, year);
                        endDateCalendar.set(Calendar.MONTH, month);
                        endDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateTextView();
                        callback.onDateSelected(DateSelected.getText().toString());
                    }
                },
                currentDateCalendar.get(Calendar.YEAR),
                currentDateCalendar.get(Calendar.MONTH),
                currentDateCalendar.get(Calendar.DAY_OF_MONTH)
        );

        // Show the date picker dialogs
        startDatePickerDialog.show();
        endDatePickerDialog.show();
    }


    private void updateDateTextView() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String startDate = dateFormat.format(startDateCalendar.getTime());
        String endDate = dateFormat.format(endDateCalendar.getTime());

        // Ensure that startDate is chronologically before endDate
        if (startDate.compareTo(endDate) <= 0) {
            DateSelected.setText("Date: " + startDate + " to " + endDate);
        } else {
            DateSelected.setText("Date: " + endDate + " to  " + startDate);
        }
    }

    private void fireStoreAccess(String categoryString, Date startDate, Date endDate, View root) {

        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a reference to the "Transactions" collection
        CollectionReference transactionsRef = db.collection("Transactions");

        Query query = transactionsRef;

        // If the category is not "All", add a filter to the query to filter by category
        if (!categoryString.equals("All")) {
            query = query.whereEqualTo("Category", categoryString);
        }

        // If both startDate and endDate are not null, add date range conditions
        if (startDate != null && endDate != null) {

            // Convert Date objects to Firestore Timestamps

            // Calculate the start of the selected day (00:00:00.000)
            Calendar startOfDayCalendar = Calendar.getInstance();
            startOfDayCalendar.setTime(startDate);
            startOfDayCalendar.set(Calendar.HOUR_OF_DAY, 0);
            startOfDayCalendar.set(Calendar.MINUTE, 0);
            startOfDayCalendar.set(Calendar.SECOND, 0);
            startOfDayCalendar.set(Calendar.MILLISECOND, 0);

            Timestamp startTimestamp = new Timestamp(startOfDayCalendar.getTime());

            // Calculate the end of the selected day (23:59:59.999)
            Calendar endOfDayCalendar = Calendar.getInstance();
            endOfDayCalendar.setTime(endDate);
            endOfDayCalendar.set(Calendar.HOUR_OF_DAY, 23);
            endOfDayCalendar.set(Calendar.MINUTE, 59);
            endOfDayCalendar.set(Calendar.SECOND, 59);
            endOfDayCalendar.set(Calendar.MILLISECOND, 999);

            Timestamp endTimestamp = new Timestamp(endOfDayCalendar.getTime());

            query = query.whereGreaterThanOrEqualTo("Date", startTimestamp);
            query = query.whereLessThanOrEqualTo("Date", endTimestamp);
        }

        // Store the data in a list
        List<Transaction> transactionArrayList = new ArrayList<>();

        // Clear the arrayList initially
        transactionArrayList.clear();

        // Execute the query
        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Check if there are any documents in the result
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Loop through the documents
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // Access the data in each document
                            Map<String, Object> data = document.getData();

                            // access specific fields
                            String amount = (String) data.get("Amount");
                            String category = (String) data.get("Category");
                            Timestamp timestamp = (Timestamp) data.get("Date");
                            String note = (String) data.get("Note");

                            // Convert Firestore Timestamp to Java Date
                            Date date = timestamp.toDate();

                            // Convert date into "yyyy-MM-dd" format
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                            String dateString = dateFormat.format(date);

                            // Create a new Transaction
                            Transaction transaction = new Transaction(amount, category, dateString, note);

                            // Add transaction to the arrayList
                            transactionArrayList.add(transaction);
                        }
                    } else {
                        // No documents found
                        Log.d(TAG, "No documents found in the 'Transactions' collection.");
                    }

                    // Create an instance of the CardAdapter once, after retrieving all data
                    CardAdapter cardAdapter = new CardAdapter(transactionArrayList);

                    // Find your RecyclerView by its ID
                    RecyclerView recyclerView = root.findViewById(R.id.transactionsRecyclerView);

                    // Create a LinearLayoutManager
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);

                    // Set the CardAdapter as the adapter for your RecyclerView
                    recyclerView.setAdapter(cardAdapter);
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error getting documents", e);
                    }
                });
    }

    // A method to get the start date and end date from the dateString and finally filter the transactions
    private void dateAndCategoryFilter (String dateString, String categoryString, View root){

        if (dateString.substring(6).equals("Today")){

            // Get the current date
            calendar = Calendar.getInstance();
            Date currentDate = calendar.getTime();

            startDate = currentDate;
            endDate = currentDate;


            categoryString = categoryString.substring(10);
        }

        else if (dateString.substring(6).equals("This week")){

            // Get the date for the beginning of the current week (Monday)
            calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            Date mondayDate = calendar.getTime();

            // Move to Sunday (end of the week)
            calendar.add(Calendar.DATE, 6);
            Date sundayDate = calendar.getTime();

            startDate = mondayDate;
            endDate = sundayDate;

            categoryString = categoryString.substring(10); // get the category selected

        }

        else if (isMonth(dateString.substring(6))){

            // Get the current month and year
            calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);

            // Set the calendar to the start of the month
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, 1); // Set to the 1st day of the month

            // Get the start date (1st day of the month)
            startDate = calendar.getTime();

            // Set the calendar to the end of the month
            calendar.add(Calendar.MONTH, 1); // Add 1 month to move to the next month
            calendar.add(Calendar.DAY_OF_MONTH, -1); // Subtract 1 day to get the last day of the current month

            // Get the end date (last day of the month)
            endDate = calendar.getTime();

            categoryString = categoryString.substring(10); // get the category selected

        }
        else {

            // Find the index of the dash "to"
            int toIndex = dateString.indexOf("to");

            if (toIndex != -1) {
                // Extract the start and end date substrings
                String startDateString = dateString.substring(toIndex - 11, toIndex - 1);
                String endDateString = dateString.substring(toIndex + 3);

                // Now, you can parse these strings into Date objects if needed
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                try {
                    startDate = dateFormat.parse(startDateString);
                    endDate = dateFormat.parse(endDateString);

                    // Now, startDate and endDate contain the parsed Date objects

                    categoryString = categoryString.substring(10); // get the category selected

                } catch (ParseException e) {
                    // Handle parsing exception
                    e.printStackTrace();
                }
            } else {
                // Handle missing to character
                System.out.println("Date range format is invalid.");
            }
        }

        // Call fireStoreAccess method to filter based the the category, startDate and endDate
        fireStoreAccess(categoryString, startDate, endDate, root);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}