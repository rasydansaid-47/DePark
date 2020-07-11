package com.example.depark.ui.slideshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SlideshowViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SlideshowViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is eParking Ticket.\n User need to scan\n QrCode to continue the\n payment.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}