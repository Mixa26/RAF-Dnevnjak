package com.example.rafdnevnjak.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.rafdnevnjak.R;
import com.example.rafdnevnjak.view.fragments.CalendarFragment;
import com.example.rafdnevnjak.view.recycler.adapter.MonthAdapter;
import com.example.rafdnevnjak.view.recycler.differ.DateDiffItemCallback;
import com.example.rafdnevnjak.viewmodels.RecyclerViewModel;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalendarFragment calendarFragment = new CalendarFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, calendarFragment).commit();
    }
}