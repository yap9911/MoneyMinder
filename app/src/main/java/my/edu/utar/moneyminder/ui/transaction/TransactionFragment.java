package my.edu.utar.moneyminder.ui.transaction;

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

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import my.edu.utar.moneyminder.R;
import my.edu.utar.moneyminder.databinding.FragmentTransactionBinding;

public class TransactionFragment extends Fragment {

    private FragmentTransactionBinding binding;
    private TextView DateSelected;
    private TextView CategorySelected;
    private Calendar startDateCalendar;
    private Calendar endDateCalendar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTransactionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DateSelected = binding.timeRange;
        CategorySelected = binding.Category;
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

                                // Get the current date
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                String currentDate = dateFormat.format(calendar.getTime());

                                // Display the current date in a Toast
                                Toast.makeText(getContext(), "Displaying today transaction ",
                                        Toast.LENGTH_SHORT).show();

                                DateSelected.setText("Date: Today");

                                return true;

                            case R.id.WeekOption:

                                // Handle WeekOption click

                                calendar = Calendar.getInstance();

                                // Get the date for the beginning of the current week (Monday)
                                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                                dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                String mondayDate = dateFormat.format(calendar.getTime());

                                // Move to Sunday (end of the week)
                                calendar.add(Calendar.DATE, 6);
                                String sundayDate = dateFormat.format(calendar.getTime());

                                // Display the date range in a Toast
                                String weekRange = mondayDate + " - " + sundayDate;
                                Toast.makeText(getContext(), "Displaying this week's transaction", Toast.LENGTH_SHORT).show();

                                DateSelected.setText("Date: This week");

                                return true;

                            case R.id.MonthOption:
                                // Handle MonthOption click

                                calendar = Calendar.getInstance();

                                // Get the current month from the Calendar
                                int currentMonth = calendar.get(Calendar.MONTH) + 1; // Adding 1 because months are 0-based

                                // Display the current month in a Toast
                                String monthString = getMonthString(currentMonth);
                                Toast.makeText(getContext(), "Displaying " + monthString + "'s transaction", Toast.LENGTH_SHORT).show();

                                DateSelected.setText("Date: " + monthString);

                                return true;

                            case R.id.CustomOption:
                                // Handle CustomOption click
                                startDateCalendar = Calendar.getInstance();
                                endDateCalendar = Calendar.getInstance();
                                showDatePickerDialog();
                                return true;

                            case R.id.FNBOption:
                                // Handle FNBOption click
                                CategorySelected.setText("Food & Beverage");
                                return true;

                            case R.id.TransportOption:
                                // Handle TransportOption click
                                CategorySelected.setText("Transportation");
                                return true;

                            case R.id.RentalsOption:
                                // Handle Rentalsoption click
                                CategorySelected.setText("Rentals");
                                return true;

                            case R.id.BillOption:
                                // Handle BillOption click
                                CategorySelected.setText("Bills");
                                return true;

                            case R.id.MedicalOption:
                                // Handle MedicalOption click
                                CategorySelected.setText("Medicals");
                                return true;

                            case R.id.FunNRelaxOption:
                                // Handle MedicalOption click
                                CategorySelected.setText("Fun & Relax");
                                return true;

                            default:
                                return false;
                        }
                    }
                });
                // Show the PopupMenu
                popupMenu.show();
            }
        });
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


    // A method to display 2 datepicker so that user can select
    // start date and end date for filter purpose
    private void showDatePickerDialog() {
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

        if (startDate.equals(endDate)) {
            // If start date and end date are the same, set text to just the start date
            DateSelected.setText("Date: " + startDate);
        }

        // Ensure that startDate is chronologically before endDate
        else if (startDate.compareTo(endDate) <= 0) {
            DateSelected.setText("Date: " + startDate + " - " + endDate);
        } else {
            DateSelected.setText("Date: " + endDate + " - " + startDate);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}