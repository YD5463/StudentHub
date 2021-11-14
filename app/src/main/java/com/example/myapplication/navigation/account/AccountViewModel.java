package com.example.myapplication.navigation.account;

import android.widget.ImageView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccountViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<ImageView> mProfileImage;

    public AccountViewModel() {
        mText = new MutableLiveData<>();
        mProfileImage = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
//        mProfileImage.setValue();
    }

    public LiveData<String> getText() {
        return mText;
    }
}