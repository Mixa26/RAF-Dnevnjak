package com.example.rafdnevnjak.view.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.rafdnevnjak.R;
import com.example.rafdnevnjak.model.Date;
import com.example.rafdnevnjak.model.Obligation;
import com.example.rafdnevnjak.view.fragments.CalendarFragment;
import com.example.rafdnevnjak.view.fragments.DailyPlanFragment;
import com.example.rafdnevnjak.view.recycler.adapter.MonthAdapter;
import com.example.rafdnevnjak.view.recycler.differ.DateDiffItemCallback;
import com.example.rafdnevnjak.view.viewpager.PagerAdapter;
import com.example.rafdnevnjak.viewmodels.CalendarViewModel;
import com.example.rafdnevnjak.viewmodels.RecyclerViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;

    private PagerAdapter pagerAdapter;

    private Date dateSelected;

    private MonthAdapter.DayViewHolder dateViewSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
        initViewPager();
        initNavigation();
    }

    /**
     * Initializes the view pager for menus: calendar, daily plan and profile
     */
    private void initViewPager(){
        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(PagerAdapter.CALENDAR, false);

        //Initial setting of the title to the corresponding month and year
        setTitle(LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + LocalDate.now().getYear() + ".");
    }

    /**
     * Initializes switched between fragments when clicking the bottom navigation buttons
     */
    private void initNavigation(){
        ((BottomNavigationView)findViewById(R.id.bottomNavigation)).setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                //We don't want the user to be able to pick the daily plan through the menu,
                //but rather through clicking on a certain date
                case R.id.calendarMenu: {
                    //Reset the date text to month text if switching from the DailyPlanFragment
                    if (dateSelected != null){
                        setTitle(dateSelected.getDate().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + dateSelected.getDate().getYear() + ".");
                    }
                    viewPager.setCurrentItem(PagerAdapter.CALENDAR, false); break;
                }
                case R.id.profileMenu: {
                    //Set the title to profile
                    setTitle(R.string.profile);
                    viewPager.setCurrentItem(PagerAdapter.PROFILE, false); break;
                }
            }
            return true;
        });
    }

    /**
     * Sets the selected date to the one clicked by the user and opens the DailyPlanFragment
     * @param dateSelected The date that has been clicked on in the calendar.
     */
    public void openDailyPlanMenu(Date dateSelected) {
        this.dateSelected = dateSelected;
        ((BottomNavigationView)findViewById(R.id.bottomNavigation)).setSelectedItemId(R.id.dailyPlanMenu);
        setTitle(dateSelected.getDate().getMonth().getDisplayName(TextStyle.FULL,Locale.ENGLISH) + " " + dateSelected.getDate().getDayOfMonth() + ". " + dateSelected.getDate().getYear() + ".");
        viewPager.setCurrentItem(PagerAdapter.DAILY_PLAN, false);
    }

    /**
     * Returns what color the date should be painted in based on the most important obligation
     * that day
     */
    public int getColorOfDayInCalendar(Date date){
        if (date == null) return 0;

        String title = date.getDate().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " +
                        date.getDate().getDayOfMonth() + ". " + date.getDate().getYear() + ".";

        CalendarViewModel calendarViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);

        int severity = 0;

        if (calendarViewModel.getObligations(title) == null) return severity;

        List<Obligation> obligations = calendarViewModel.getObligations(title).getValue();

        if (obligations == null)return 0;

        for (Obligation obligation : obligations){
            if (obligation.getObligationSeverity().equals(Obligation.ObligationSeverity.LOW)){
                if (severity < 1) {
                    severity = 1;
                }
            }
            else if (obligation.getObligationSeverity().equals(Obligation.ObligationSeverity.MID)){
                if (severity < 2) {
                    severity = 2;
                }
            }
            else{
                severity = 3;
            }
        }

        return severity;
    }

    public Date getDateSelected() {
        return dateSelected;
    }

    public void setDateViewSelected(MonthAdapter.DayViewHolder dateViewSelected) {
        this.dateViewSelected = dateViewSelected;
    }

    public MonthAdapter.DayViewHolder getDateViewSelected() {
        return dateViewSelected;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){
            DailyPlanFragment.updateObligation(getTitle().toString(), data.getParcelableExtra("oldObligation"), data.getParcelableExtra("obligation"));
        }
    }
}