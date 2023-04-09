package com.example.rafdnevnjak.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rafdnevnjak.model.Obligation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CalendarViewModel extends ViewModel {

    private final HashMap<String, MutableLiveData<List<Obligation>>> obligations = new HashMap<>();

    private final HashMap<String, ArrayList<Obligation>> obligationsList = new HashMap<>();

    public CalendarViewModel(){
    }

    public void setMutableLiveData(String dataKey){
        obligations.put(dataKey, new MutableLiveData<>());
        obligationsList.put(dataKey, new ArrayList<>());
    }

    /**
     * Adds a new obligation for the given date
     * @param dateKey is a key that has a format (Month dd. yyyy.) for example April 23. 2023.
     * @param obligation The obligation to be added.
     */
    public void addObligation(String dateKey, Obligation obligation){
        ArrayList<Obligation> curr = obligationsList.get(dateKey);

        //If the list didn't have any other obligations for this date yet
        if (curr != null){
            //Searching for the place to insert our obligation
            int i = findInsertPosition(0, curr, obligation);

            curr.add(i, obligation);
            ArrayList<Obligation> listToSubmit = new ArrayList<>(curr);
            obligations.get(dateKey).setValue(listToSubmit);
        }
        //Else the list has obligations for this date so we add a new one
        else{
            curr = new ArrayList<>();
            curr.add(obligation);
            obligationsList.put(dateKey, curr);
            MutableLiveData<List<Obligation>> mld = new MutableLiveData<>();
            ArrayList<Obligation> listToSubmit = new ArrayList<>(curr);
            mld.setValue(listToSubmit);
            obligations.replace(dateKey, mld);
        }
    }

    /**
     * Deletes an existing obligation
     * @param dateKey string key of the date in the format (Day mm. yyyy.) for example April 23. 2023.
     * @param obligation a obligation object to be deleted
     */
    public void deleteObligation(String dateKey, Obligation obligation) {
        ArrayList<Obligation> currObligations = obligationsList.get(dateKey);
        int toDelete = -1;

        for (int i = 0; i < currObligations.size(); i++){
            if (currObligations.get(i).getTitle().equals(obligation.getTitle())
                    && currObligations.get(i).getStartHour() == obligation.getStartHour()
                    && currObligations.get(i).getStartMinute() == obligation.getStartMinute()
                    && currObligations.get(i).getEndHour() == obligation.getEndHour()
                    && currObligations.get(i).getEndMinute() == obligation.getEndMinute())
            {
                toDelete = i;
                break;
            }
        }

        obligationsList.get(dateKey).remove(toDelete);
        obligations.get(dateKey).setValue(new ArrayList<>(obligationsList.get(dateKey)));
    }

    /**
     * Updates the obligation data
     * @param dateKey string key of the date in the format (Day mm. yyyy.) for example April 23. 2023.
     * @param oldObligation old obligation object
     * @param newObligation new obligation object (with the new data)
     */
    public void updateObligation(String dateKey, Obligation oldObligation, Obligation newObligation) {
        ArrayList<Obligation> currObligations = obligationsList.get(dateKey);
        int toUpdate = -1;

        for (int i = 0; i < currObligations.size(); i++){
            if (currObligations.get(i).getTitle().equals(oldObligation.getTitle())
                    && currObligations.get(i).getStartHour() == oldObligation.getStartHour()
                    && currObligations.get(i).getStartMinute() == oldObligation.getStartMinute()
                    && currObligations.get(i).getEndHour() == oldObligation.getEndHour()
                    && currObligations.get(i).getEndMinute() == oldObligation.getEndMinute())
            {
                toUpdate = i;
                break;
            }
        }

        obligationsList.get(dateKey).set(toUpdate, newObligation);
        obligations.get(dateKey).setValue(new ArrayList<>(obligationsList.get(dateKey)));
    }

    /**
     * Returns the index at which the element should be inserted based on the sorting parameter
     * @param sortParam 0 for sorting by time only, 1 for sorting by time and LOW obligation severity
     *                  first, 2 for sorting by time and MID obligation severity and 3 for
     *                  by time and HIGH obligation severity first
     * @param curr ArrayList in which to search the index position
     * @param obligation The obligation which you want to be added to the curr array
     * @return the index at which the element should be inserted
     */
    private int findInsertPosition(int sortParam, ArrayList<Obligation> curr, Obligation obligation){

        if (sortParam == 0) {
            if (curr.size() != 0) {
                int i = 0;
                for (; i < curr.size(); i++) {
                    int obligationStart = obligation.getStartHour() * 100 + obligation.getStartMinute();
                    int obligationEnd = obligation.getEndHour() * 100 + obligation.getEndMinute();
                    int currStart = curr.get(i).getStartHour() * 100 + curr.get(i).getStartMinute();
                    int beforeCurrEnd = 0;
                    if (i != 0) {
                        beforeCurrEnd = curr.get(i - 1).getEndHour() * 100 + curr.get(i - 1).getEndMinute();
                    }

                    if (obligationEnd <= currStart) {
                        if (beforeCurrEnd <= obligationStart) {
                            break;
                        }
                    }
                }
                return i;
            } else {
                return 0;
            }
        }
        else if (sortParam == 1){

        }
        else if (sortParam == 2){

        }
        else{

        }
        return 0;
    }

    public MutableLiveData<List<Obligation>> getObligations(String dateKey) {
        MutableLiveData<List<Obligation>> obligationsToReturn = obligations.get(dateKey);

        if (obligationsToReturn == null)return null;

        return obligationsToReturn;
    }

    /**
     * Checks if there is overlapping obligations and return false in that case
     * @param dateKey The date formated as (Month dd. yyyy.) for example April 23. 2023.
     * @param obligation The obligation to check the time availability for.
     * @return false if there are overlapping obligations at the provided time, true otherwise
     */
    public boolean checkTimeAvailability(String dateKey, Obligation obligation){
        ArrayList<Obligation> dailyObligations = obligationsList.get(dateKey);

        //This shouldn't happen, but we ensure the sanity check
        if (dailyObligations == null) return false;

        //If list is empty, there is surely time for the obligation
        if (dailyObligations.size() == 0) return true;

        //This sorts all the obligations by their finishing time and then by its beginning time
        Comparator<Obligation> byTime = Comparator.comparing(Obligation::getEndHour)
                .thenComparing(Obligation::getEndMinute)
                .thenComparing(Obligation::getStartHour)
                .thenComparing(Obligation::getStartMinute);

        dailyObligations.sort(byTime);

        //Check the overlap based on the end of the obligation to be added and the start
        //of the obligation existing in the list, and then check the start time of the
        //obligation to be added and the end of the previous one in the list
        int obligationStart = 0;
        int obligationEnd = 0;
        int currStart = 0;
        int beforeCurrEnd = 0;
        for (int i = 0; i < dailyObligations.size(); i++){
            obligationStart = obligation.getStartHour() * 100 + obligation.getStartMinute();
            obligationEnd = obligation.getEndHour() * 100 + obligation.getEndMinute();
            currStart = dailyObligations.get(i).getStartHour() * 100 + dailyObligations.get(i).getStartMinute();
            beforeCurrEnd = 0;
            if (i != 0) {
                beforeCurrEnd = dailyObligations.get(i - 1).getEndHour() * 100 + dailyObligations.get(i-1).getEndMinute();
            }

            if (obligationEnd <= currStart){
                if (beforeCurrEnd > obligationStart){
                    return false;
                }
                return true;
            }
        }
        beforeCurrEnd = dailyObligations.get(dailyObligations.size()-1).getEndHour() * 100 + dailyObligations.get(dailyObligations.size()-1).getEndMinute();

        return obligationStart >= beforeCurrEnd;
    }
}
