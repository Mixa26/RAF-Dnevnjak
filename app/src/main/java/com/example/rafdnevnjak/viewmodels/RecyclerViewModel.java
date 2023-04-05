package com.example.rafdnevnjak.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rafdnevnjak.model.Date;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Saves calendar data so we can show it to the user
 */
public class RecyclerViewModel extends ViewModel {

    private final MutableLiveData<List<Date>> dates = new MutableLiveData<>();

    private ArrayList<Date> datesList = new ArrayList<Date>();

    public RecyclerViewModel(){
        //We get the current date so we can display the current month in the calendar to the user
        Date currDate = new Date(LocalDate.now());

        //Go to the first of the month
        currDate = new Date(currDate.getDate().minusDays(currDate.getDate().getDayOfMonth()));

        //Find the first monday so we can show the calendar with monday as the first day
        while(!currDate.getDate().getDayOfWeek().equals(DayOfWeek.MONDAY)){
            currDate = new Date(currDate.getDate().minusDays(1));
        }

        int daysToAdd = 38;

        //Add dates to the datesList so we can move it to the dates later
        for (int i = 0; i < daysToAdd; i++){
            datesList.add(new Date(currDate.getDate().plusDays(i)));
        }

        //A little hack so that the mutable live date registers the change of values
        ArrayList<Date> listToSubmit = new ArrayList<>(datesList);
        dates.setValue(listToSubmit);
    }

    public MutableLiveData<List<Date>> getDates() {
        return dates;
    }
}
