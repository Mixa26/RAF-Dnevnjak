package com.example.rafdnevnjak.view.activities;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.rafdnevnjak.R;
import com.example.rafdnevnjak.model.Obligation;
import com.example.rafdnevnjak.view.fragments.DailyPlanFragment;
import com.example.rafdnevnjak.viewmodels.CalendarViewModel;
import com.example.rafdnevnjak.viewmodels.RecyclerViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;
import java.util.Objects;

public class ObligationActivity extends AppCompatActivity {

    //The calendar view model which will store data for obligations of each day
    private CalendarViewModel calendarViewModel;

    //Text views which are used as "buttons" for opening the time picker
    private TextView startTimeTextView;

    private TextView endTimeTextView;

    //Actual time pickers for the start and end time of the task
    private TimePickerDialog startTimePickerDialog;

    private TimePickerDialog endTimePickerDialog;

    //Tab layout which will be used for picking the priority of the task (High, Mid, Low)
    private TabLayout tabLayout;

    //Title of the obligation
    private EditText title;

    //Description of the obligation
    private EditText description;

    //Create button which when pressed takes all the data from the obligation creation screen,
    //and makes a new obligation, then closes this activity
    private Button createButton;

    //Cancel button which closes this activity without further action
    private Button cancelButton;

    //True if the start time has been selected, false otherwise
    private boolean startTimeSelected;

    //True if the end time has been selected, false otherwise
    private boolean endTimeSelected;

    //0 is Low, 1 is Mid and 2 is High
    private int indexOfTabSelected;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obligation);

        //Set that the time for start and end of obligation are not selected
        startTimeSelected = false;
        endTimeSelected = false;

        //Set the title to be the date for which we are creating the obligation
        setTitle(getIntent().getStringExtra("title"));

        //Default selected tab is Low which is at index 0
        indexOfTabSelected = 0;

        init();
    }

    /**
     * Initializes all the init functions
     */
    private void init(){
        initView();
        initTimePicker();
        initListeners();
    }

    /**
     * Get all the necessary view components inside code so we can work with them
     */
    private void initView(){
        startTimeTextView = findViewById(R.id.start_time_textview);
        endTimeTextView = findViewById(R.id.end_time_textview);

        title = findViewById(R.id.obligationTitleText);

        tabLayout = findViewById(R.id.obligationLevel);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setText(R.string.low_importance);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setText(R.string.mid_importance);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setText(R.string.high_importance);

        description = findViewById(R.id.obligationDescriptionText);

        createButton = findViewById(R.id.createButton);
        cancelButton = findViewById(R.id.cancelButton);
    }

    /**
     * Initializes the time picker screen so when the user opens it, it shows the current time
     */
    private void initTimePicker(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        //Opens the Time picker for the task start time with the time set to current time
        startTimePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minuteOfDay) -> {
                    //Check if the selected start time is before the selected end time
                    if (endTimeSelected){
                        if (hourOfDay > Integer.parseInt(endTimeTextView.getText().toString().substring(0,2))
                        || (hourOfDay == Integer.parseInt(endTimeTextView.getText().toString().substring(0,2))
                        && (minuteOfDay > Integer.parseInt(endTimeTextView.getText().toString().substring(3,5))))){
                            //Start time is after end time, this is not good
                            Toast.makeText(this, getText(R.string.bad_time_selection_alert), Toast.LENGTH_LONG).show();
                        }
                        else{
                            startTimeTextView.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minuteOfDay));
                            startTimeSelected = true;
                        }
                    }
                    else{
                        startTimeTextView.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minuteOfDay));
                        startTimeSelected = true;
                    }
                },
                hour,
                minute,
                true
        );

        //Opens the Time picker for the task finish time with the time set to current time
        endTimePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minuteOfDay) -> {
                    //Check if the selected end time is after the selected start time
                    if (startTimeSelected){
                        if (hourOfDay < Integer.parseInt(startTimeTextView.getText().toString().substring(0,2))
                        || (hourOfDay == Integer.parseInt(startTimeTextView.getText().toString().substring(0,2))
                        && (minuteOfDay < Integer.parseInt(startTimeTextView.getText().toString().substring(3,5))))){
                            //End time is before end time, this is not good
                            Toast.makeText(this, getText(R.string.bad_time_selection_alert), Toast.LENGTH_LONG).show();
                        }
                        else {
                            endTimeTextView.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minuteOfDay));
                            endTimeSelected = true;
                        }
                    }
                    else{
                        endTimeTextView.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minuteOfDay));
                        endTimeSelected = true;
                    }
                },
                hour,
                minute,
                true
        );
    }

    /**
     * Initializes all the listeners for button presses and other
     */
    private void initListeners(){
        //If the Select start time text is clicked open the clock
        startTimeTextView.setOnClickListener(v -> {
            startTimePickerDialog.show();
        });

        //If the Select end time text is clicked open the clock
        endTimeTextView.setOnClickListener(v -> {
            endTimePickerDialog.show();
        });

        //Change the index of the selected tab when the selection occurs
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                indexOfTabSelected = tab.getPosition();
            }

            //These are not needed so we leave them empty
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        //If the create button is pressed, pull all the data and make a new obligation,
        //then close the activity
        createButton.setOnClickListener(v -> {
            //Check if everything is okay with input and proceed to make the new obligation
            if (checkInput()) {
                int startHour = Integer.parseInt(startTimeTextView.getText().toString().substring(0,2));
                int startMinute = Integer.parseInt(startTimeTextView.getText().toString().substring(3,5));

                int endHour = Integer.parseInt(endTimeTextView.getText().toString().substring(0,2));
                int endMinute = Integer.parseInt(endTimeTextView.getText().toString().substring(3,5));

                Obligation.ObligationSeverity obligationSeverity;

                if (indexOfTabSelected == 0){
                    obligationSeverity = Obligation.ObligationSeverity.LOW;
                }
                else if (indexOfTabSelected == 1) {
                    obligationSeverity = Obligation.ObligationSeverity.MID;
                }
                else{
                    obligationSeverity = Obligation.ObligationSeverity.HIGH;
                }

                Obligation obligation = new Obligation(title.getText().toString(),
                        startHour, startMinute, endHour, endMinute, description.getText().toString(), obligationSeverity);

                //calendarViewModel.addObligation(getIntent().getStringExtra("title"), obligation);

                Intent intent = new Intent();
                intent.putExtra("obligation", obligation);
                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        });

        //If the cancel button is pressed close this activity
        cancelButton.setOnClickListener(v -> {
            finish();
        });
    }

    //Checks if all the necessary input has been filled
    private boolean checkInput(){
        if (!startTimeSelected || !endTimeSelected){
            Toast.makeText(this, getText(R.string.time_not_selected_alert), Toast.LENGTH_LONG).show();
            return false;
        }

        if (title.getText().toString().equals("")){
            Toast.makeText(this, getText(R.string.obligation_text_empty_alert), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
