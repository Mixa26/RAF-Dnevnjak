package com.example.rafdnevnjak.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rafdnevnjak.R;
import com.example.rafdnevnjak.model.Date;
import com.example.rafdnevnjak.model.Obligation;
import com.example.rafdnevnjak.view.activities.MainActivity;
import com.example.rafdnevnjak.view.activities.ObligationActivity;
import com.example.rafdnevnjak.view.recycler.adapter.ObligationAdapter;
import com.example.rafdnevnjak.view.recycler.differ.ObligationDiffItemCallback;
import com.example.rafdnevnjak.viewmodels.CalendarViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DailyPlanFragment extends Fragment {

    //The calendar view model which will store data for obligations of each day
    private static CalendarViewModel calendarViewModel;

    //Tab layout which is used to filter the obligations by priority (High, Mid, Low)
    private TabLayout tabLayout;

    //This is gonna hold our obligations
    private RecyclerView recyclerView;

    //The adapter for the recyclerView
    private ObligationAdapter obligationAdapter;

    //The view which we need to use to get the view components calling the function
    //findViewById()
    private View view;

    //A button that opens the ObligationActivity so we can create a new Obligation
    private FloatingActionButton addObligationButton;

    //The switch that enables/disables past obligations to be viewed
    private SwitchCompat showPastObligationsSwitch;

    //Search input
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dailyplan, container, false);
        calendarViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);

        init();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshObligationsView();
    }

    @Override
    public void onPause() {
        super.onPause();

        searchView.setQuery("",false);
        searchView.clearFocus();
    }

    /**
     * Refreshes the rendering of the recycler view which shows obligations
     */
    public void refreshObligationsView(){
        setUpObligationsView();
        //TODO fix coloring of past events
        //colorPastObligations();
    }

    /**
     * Checks if a obligation has passed, and colors it gray if so
     */
    private void colorPastObligations(){
        Date selectedDate = ((MainActivity)getActivity()).getDateSelected();

        if ((selectedDate.getDate().isEqual(LocalDate.now()))){
            List<Obligation> obligationsModels = obligationAdapter.getCurrentList();
            for (int i = 0; i < obligationAdapter.getItemCount(); i++){
                if (obligationsModels.get(i).getEndHour() < LocalTime.now().getHour() ||
                        (obligationsModels.get(i).getEndHour() == LocalTime.now().getHour()
                && obligationsModels.get(i).getEndMinute() < LocalTime.now().getMinute())) {
                    ((ObligationAdapter.ObligationViewHolder) recyclerView.findViewHolderForAdapterPosition(i)).
                            itemView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                }
            }
        }
        else if (selectedDate.getDate().isBefore(LocalDate.now())){
            for (int i = 0; i < obligationAdapter.getItemCount(); i++){
                ((ObligationAdapter.ObligationViewHolder)recyclerView.findViewHolderForAdapterPosition(i)).
                        itemView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.light_gray));
            }
        }


    }

    /**
     * Initializes RecyclerView which shows obligations for the selected date
     */
    private void setUpObligationsView(){
        //Initialize the MutableLiveData list right away so we don't get null pointer on observing
        //in the initializeListeners() function
        if (calendarViewModel.getObligations(getActivity().getTitle().toString()) == null) {
            calendarViewModel.setMutableLiveData(getActivity().getTitle().toString());
            obligationAdapter.submitList(new ArrayList<>());

            calendarViewModel.getObligations(getActivity().getTitle().toString()).observe(getViewLifecycleOwner(), this::addAvailableObligations);
        }
        else{
            addAvailableObligations(calendarViewModel.getObligations(getActivity().getTitle().toString()).getValue());
        }
    }

    /**
     * Adds all obligations except the ones in the past to the view
     * @param obligations the list of all obligations for the selected date
     */
    private void addAvailableObligations(List<Obligation> obligations){
        if (obligations == null)return;
        obligationAdapter.submitList(new ArrayList<>());
        Date selectedDate = ((MainActivity)getActivity()).getDateSelected();

        if (showPastObligationsSwitch.isChecked()){
            obligationAdapter.submitList(obligations);
            return;
        }

        if ((selectedDate.getDate().isEqual(LocalDate.now()))){
            ArrayList<Obligation> obligationsToAdd = new ArrayList<>();
            //If selected date is today's date, check which obligations haven't passed
            for (Obligation obligation : obligations){
                if (obligation.getEndHour() > LocalTime.now().getHour()){
                    obligationsToAdd.add(obligation);
                }
                else if (obligation.getEndHour() == LocalTime.now().getHour()){
                    if (obligation.getEndMinute() >= LocalTime.now().getMinute()) {
                        obligationsToAdd.add(obligation);
                    }
                }
            }
            obligationAdapter.submitList(new ArrayList<>(obligationsToAdd));
        }
        else if (selectedDate.getDate().isAfter(LocalDate.now())){
            //If selected date is after today's date, add all obligations
            obligationAdapter.submitList(obligations);
        }
        //Else the date is in the past and we don't show obligations unless its selected
        //by the user (by default it's not)
    }

    /**
     * Responsible for initializing all the other init functions
     */
    private void init(){
        initView();
        initRecycler();
        initListeners();
    }

    /**
     * Gets all the view components inside code so we can work with them
     */
    private void initView(){
        recyclerView = view.findViewById(R.id.recyclerViewDailyPlan);

        showPastObligationsSwitch = view.findViewById(R.id.switch_obligations);
        searchView = view.findViewById(R.id.searchBar);

        tabLayout = view.findViewById(R.id.sortTabMenu);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setText(R.string.low_importance);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setText(R.string.mid_importance);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setText(R.string.high_importance);
        tabLayout.selectTab(null);

        addObligationButton = view.findViewById(R.id.addObligationButton);
    }

    /**
     * Initializes the recycler view that will show the obligations
     */
    private void initRecycler(){
        obligationAdapter = new ObligationAdapter(new ObligationDiffItemCallback());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(obligationAdapter);
    }

    /**
     * Initializes all the listeners for button presses and similar
     */
    private void initListeners(){

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return onQueryTextChange(query);
            }

            //Update the search as the user types
            @Override
            public boolean onQueryTextChange(String newText) {
                if (calendarViewModel.getObligations(getActivity().getTitle().toString()) == null ||
                        calendarViewModel.getObligations(getActivity().getTitle().toString()).getValue() == null) return false;
                List<Obligation> originalList = calendarViewModel.getObligations(getActivity().getTitle().toString()).getValue();

                ArrayList<Obligation> filteredList = new ArrayList<>();
                for (Obligation obligation : originalList){
                    if (obligation.getTitle().contains(newText)){
                        filteredList.add(obligation);
                    }
                }

                addAvailableObligations(new ArrayList<>(filteredList));

                return true;
            }
        });

        showPastObligationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if (isChecked) {
                     obligationAdapter.submitList(calendarViewModel.getObligations(getActivity().getTitle().toString()).getValue());
                 } else {
                     addAvailableObligations(calendarViewModel.getObligations(getActivity().getTitle().toString()).getValue());
                 }
             }});

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //Sorts obligations based on which priority tab is selected
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getId() == R.id.lowTab){
                    //TODO sort the tasks low to high
                }
                else if (tab.getId() == R.id.midTab) {
                    //TODO sort the tasks mid to high
                }
                else {
                    //TODO sort the tasks high to low
                }
            }

            //Not needed so we just leave them empty
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        //When the add button is pressed it opens the ObligationActivity so we can
        //create a new obligation for the selected date
        //We receive the obligation later through the intent
        addObligationButton.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ObligationActivity.class);
            intent.putExtra("title", getActivity().getTitle());
            startActivityForResult(intent, 1);
        });

    }


    //This is where we pick up the created obligation from the ObligationActivity
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Obligation obligation = data.getParcelableExtra("obligation");
            if (calendarViewModel.checkTimeAvailability(getActivity().getTitle().toString(), obligation)) {
                calendarViewModel.addObligation(getActivity().getTitle().toString(), obligation);
            }
            else{
                Toast.makeText(getActivity(), R.string.obligation_time_overlap, Toast.LENGTH_LONG).show();
            }
        }
    }

    public static void deleteObligation(String dateKey, Obligation obligation){
        calendarViewModel.deleteObligation(dateKey, obligation);
    }
}
