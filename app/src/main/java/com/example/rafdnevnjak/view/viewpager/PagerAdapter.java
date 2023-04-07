package com.example.rafdnevnjak.view.viewpager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.rafdnevnjak.view.fragments.CalendarFragment;
import com.example.rafdnevnjak.view.fragments.DailyPlanFragment;
import com.example.rafdnevnjak.view.fragments.ProfileFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    //Each of these items corresponds to a Fragment view to be displayed
    private final int ITEM_COUNT = 3;
    public static final int CALENDAR = 0;
    public static final int DAILY_PLAN = 1;
    public static final int PROFILE = 2;

    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case CALENDAR: fragment = new CalendarFragment(); break;
            case DAILY_PLAN: fragment = new DailyPlanFragment(); break;
            default: fragment = new ProfileFragment(); break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return ITEM_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case CALENDAR: return "Calendar";
            case DAILY_PLAN: return "Daily plan";
            default: return "Profile";
        }
    }
}