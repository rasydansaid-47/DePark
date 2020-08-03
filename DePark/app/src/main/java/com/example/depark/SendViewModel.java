package com.example.depark;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SendViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SendViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Contact Us!\nPlease fill this form in a decent manner");
    }

    public LiveData<String> getText() {
        return mText;
    }
}