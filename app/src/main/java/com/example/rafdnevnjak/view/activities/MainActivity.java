package com.example.rafdnevnjak.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.rafdnevnjak.R;
import com.example.rafdnevnjak.model.Date;
import com.example.rafdnevnjak.view.fragments.CalendarFragment;
import com.example.rafdnevnjak.view.fragments.DailyPlanFragment;
import com.example.rafdnevnjak.view.recycler.adapter.MonthAdapter;
import com.example.rafdnevnjak.view.recycler.differ.DateDiffItemCallback;
import com.example.rafdnevnjak.view.viewpager.PagerAdapter;
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
}