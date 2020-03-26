package com.example.aktuelmarketapp.ui.Favori;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FavoriViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FavoriViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}