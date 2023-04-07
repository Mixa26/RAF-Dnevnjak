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

    //This class keeps track if there are changes made and notices the other
    //components which are observing changes (like the view for example)
    private final MutableLiveData<List<Date>> dates = new MutableLiveData<>();

    //This array list is just used so we can submit data to the list above,
    //else it doesn't notice the changes
    private ArrayList<Date> datesList = new ArrayList<Date>();

    public RecyclerViewModel(){
        //We get the current date so we can display the current month in the calendar to the user
        Date currDate = new Date(LocalDate.now());

        //Go to the first of the month
        currDate = new Date(currDate.getDate().minusDays(currDate.getDate().getDayOfMonth()));

        int daysToAdd = currDate.getDate().lengthOfMonth();

        //Find the first monday so we can show the calendar with monday as the first day
        while(!currDate.getDate().getDayOfWeek().equals(DayOfWeek.MONDAY)){
            currDate = new Date(currDate.getDate().minusDays(1));
            daysToAdd += 1;
        }

        //Add dates to the datesList so we can move it to the dates later
        for (int i = 0; i < daysToAdd; i++){
            datesList.add(new Date(currDate.getDate().plusDays(i)));
        }

        //A little hack so that the mutable live date registers the change of values
        ArrayList<Date> listToSubmit = new ArrayList<>(datesList);
        dates.setValue(listToSubmit);
    }

    /**
     * Loads upcoming month at the bottom of calendar
     */
    public void addMonth(){
        int daysToAdd = datesList.get(datesList.size()-1).getDate().lengthOfMonth();

        //Add dates to the datesList so we can move it to the dates later
        for (int i = 0; i < daysToAdd; i++){
            Date date = new Date(datesList.get(datesList.size()-1).getDate().plusDays(1));
            datesList.add(date);
        }

        //A little hack so that the mutable live date registers the change of values
        ArrayList<Date> listToSubmit = new ArrayList<>(datesList);
        dates.setValue(listToSubmit);
    }

    /**
     * Loads past month at the top of the calendar
     */
    public void addMonthToBeginning(){
        int daysToAdd = 0;

        //Calculates how many days it has to add to the top because we might've already
        //added some days from the past month to format the calendar so it starts with monday
        //as the first day
        if (datesList.get(0).getDate().minusDays(1).getMonth().equals(datesList.get(0).getDate().getMonth())){
            daysToAdd = datesList.get(0).getDate().getDayOfMonth()-1;
        }
        else{
            daysToAdd = datesList.get(0).getDate().minusDays(1).lengthOfMonth();
        }

        //Add dates to the datesList so we can move it to the dates later
        for (int i = 0; i < daysToAdd; i++){
            Date date = new Date(datesList.get(0).getDate().minusDays(1));
            datesList.add(0, date);
        }

        Date date = new Date(datesList.get(0).getDate());
        //Add days so that the calendar starts with a monday
        while (!date.getDate().getDayOfWeek().equals(DayOfWeek.MONDAY)){
            date = new Date(datesList.get(0).getDate().minusDays(1));
            datesList.add(0, date);
        }

        //A little hack so that the mutable live date registers the change of values
        ArrayList<Date> listToSubmit = new ArrayList<>(datesList);
        dates.setValue(listToSubmit);
    }

    public MutableLiveData<List<Date>> getDates() {
        return dates;
    }

    public ArrayList<Date> getDatesList() {
        return datesList;
    }
}
