package com.example.asg1.calculate;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CalculateViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CalculateViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}