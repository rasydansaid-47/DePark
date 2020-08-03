package com.example.depark;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("USERNAME\n EMAIL\n PHONENUMBER\n");
    }

    public LiveData<String> getText() {
        return mText;
    }
}