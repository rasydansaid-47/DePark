package com.example.depark.ui.share;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShareViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ShareViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("USERNAME\n EMAIL\n PHONENUMBER\n");
    }

    public LiveData<String> getText() {
        return mText;
    }
}