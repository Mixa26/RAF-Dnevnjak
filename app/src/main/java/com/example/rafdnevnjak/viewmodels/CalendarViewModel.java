package com.example.rafdnevnjak.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rafdnevnjak.model.Obligation;

import java.util.ArrayList;
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
            curr.add(obligation);
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

    public MutableLiveData<List<Obligation>> getObligations(String dateKey) {
        MutableLiveData<List<Obligation>> obligationsToReturn = obligations.get(dateKey);

        if (obligationsToReturn == null)return null;

        return obligationsToReturn;
    }
}
