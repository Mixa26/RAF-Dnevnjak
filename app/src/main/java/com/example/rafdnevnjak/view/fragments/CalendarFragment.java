package com.example.rafdnevnjak.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rafdnevnjak.R;
import com.example.rafdnevnjak.model.Date;
import com.example.rafdnevnjak.view.recycler.adapter.MonthAdapter;
import com.example.rafdnevnjak.view.recycler.differ.DateDiffItemCallback;
import com.example.rafdnevnjak.viewmodels.RecyclerViewModel;

import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

public class CalendarFragment extends Fragment {

    private RecyclerView recyclerView;

    private RecyclerViewModel recyclerViewModel;

    private MonthAdapter monthAdapter;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        recyclerViewModel = new ViewModelProvider(this).get(RecyclerViewModel.class);

        init();

        return view;
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
        recyclerView = view.findViewById(R.id.recyclerViewCalendar);
    }

    /**
     * Initializes listeners so they can respond to actions
     */
    private void initObservers(){
        recyclerViewModel.getDates().observe(getViewLifecycleOwner(), dates -> {
            monthAdapter.submitList(dates);
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //Sets the title of the corresponding month the user is looking at when scrolling down
                if (dy > 0){
                    Date date = recyclerViewModel.getDatesList().get(((GridLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findLastVisibleItemPosition());
                    if (date.getDate().getDayOfMonth() >= 25){
                        getActivity().setTitle(date.getDate().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + date.getDate().getYear() + ".");
                    }
                }

                //Sets the title of the corresponding month the user is looking at when scrolling up
                if (dy < 0){
                    Date date = recyclerViewModel.getDatesList().get(((GridLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findFirstVisibleItemPosition());
                    if (date.getDate().getDayOfMonth() <= 15){
                        getActivity().setTitle(date.getDate().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + date.getDate().getYear() + ".");
                    }
                }

                //Loads the next month when user scrolls to the bottom of the calendar
                if (recyclerView.findFocus() != null)
                    if (!recyclerView.findFocus().canScrollVertically(1)) {
                        recyclerViewModel.addMonth();
                    }

                //Loads the previous month when user scrolls to the top of the calendar
                if (recyclerView.findFocus() != null)
                    if (!recyclerView.findFocus().canScrollVertically(-1)) {
                        recyclerViewModel.addMonthToBeginning();
                    }
            }
        });
    }

    /**
     * Initializes the recycler view that will show the calendar
     */
    private void initRecycler(){
        monthAdapter = new MonthAdapter(new DateDiffItemCallback(), date -> {});
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        recyclerView.setAdapter(monthAdapter);
    }
}
