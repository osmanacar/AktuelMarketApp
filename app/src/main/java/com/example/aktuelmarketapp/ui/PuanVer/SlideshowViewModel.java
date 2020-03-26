package com.example.aktuelmarketapp.ui.PuanVer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SlideshowViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SlideshowViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("PUANVER FRAGMENT");
    }

    public LiveData<String> getText() {
        return mText;
    }
}