package com.example.rafdnevnjak.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.rafdnevnjak.R;
import com.example.rafdnevnjak.view.recycler.adapter.MonthAdapter;
import com.example.rafdnevnjak.view.recycler.differ.DateDiffItemCallback;
import com.example.rafdnevnjak.viewmodels.RecyclerViewModel;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private RecyclerViewModel recyclerViewModel;

    private MonthAdapter monthAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewModel = new ViewModelProvider(this).get(RecyclerViewModel.class);

        init();
    }

    /**
     * Initializes all the necessary components
     */
    private void init(){
        initView();
        initRecycler();
        initObservers();
    }

    /**
     * Finds view elements from the screen so we can use their data later.
     */
    private void initView(){
        recyclerView = findViewById(R.id.recyclerView);
    }

    /**
     * Initializes listeners so they can respond to actions
     */
    private void initObservers(){
        recyclerViewModel.getDates().observe(this, dates -> {
            monthAdapter.submitList(dates);
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!getCurrentFocus().canScrollVertically(1)) {
                    recyclerViewModel.addMonth();
                }

                if (!getCurrentFocus().canScrollVertically(-1)) {
                    recyclerViewModel.addMonthToBeginning();
                }
            }
        });
    }

    /**
     * Initializes the recycler view that will show the calendar
     */
    private void initRecycler(){
        monthAdapter = new MonthAdapter(new DateDiffItemCallback(), date -> {
            //TODO open the tasks for that date
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this, 7));
        recyclerView.setAdapter(monthAdapter);
    }
}