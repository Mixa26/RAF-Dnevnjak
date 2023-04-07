package com.example.rafdnevnjak.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

/**
 * Simple class used for showing the splash screen when the app starts
 */
public class SplashViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(true);

    public SplashViewModel() {
        try {
            Thread.sleep(2000);
            isLoading.setValue(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

}