package com.example.rafdnevnjak.view.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rafdnevnjak.R;
import com.example.rafdnevnjak.model.Obligation;
import com.example.rafdnevnjak.view.fragments.DailyPlanFragment;

import java.util.Locale;

public class DetailedObligationActivity extends AppCompatActivity {

    //Text to represent time
    private TextView timeText;

    //Text to represent the title of the obligation
    private TextView titleText;

    //EditText for the description of the obligation
    private TextView descriptionText;

    //Button for editing the obligation
    private Button editButton;

    //Button for deleting the obligation
    private Button deleteButton;

    //The obligation we are seeing
    private Obligation obligation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_obligation);
        setTitle(getIntent().getStringExtra("title"));

        init();
    }

    /**
     * Responsible for initializing all the other init functions
     */
    private void init(){
        initView();
        initListeners();
    }

    /**
     * Gets all the view components inside code so we can work with them
     */
    private void initView(){
        timeText = findViewById(R.id.timeDetailedObligation);
        titleText = findViewById(R.id.titleDetailedObligation);
        descriptionText = findViewById(R.id.descriptionDetailedObligation);
        editButton = findViewById(R.id.editDetailedObligation);
        deleteButton = findViewById(R.id.deleteDetailedObligation);

        obligation = getIntent().getParcelableExtra("obligation");

        //Set up obligation details on the view
        timeText.setText(new String(String.format(Locale.getDefault(), "%02d:%02d - %02d:%02d",
                obligation.getStartHour(), obligation.getStartMinute(), obligation.getEndHour(), obligation.getEndMinute())));
        titleText.setText(obligation.getTitle());
        descriptionText.setText(obligation.getDescription());
    }

    /**
     * Initializes all the listeners for button presses and similar
     */
    private void initListeners(){
        editButton.setOnClickListener(v -> {

        });

        deleteButton.setOnClickListener(v -> {
            DailyPlanFragment.deleteObligation(getTitle().toString(), obligation);

            finish();
        });
    }
}
