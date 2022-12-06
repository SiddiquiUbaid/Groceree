package com.example.groceree.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.groceree.repository.AuthenticationRepository;
import com.google.firebase.auth.FirebaseUser;


public class LoggedInViewModel extends AndroidViewModel {
    private AuthenticationRepository authAppRepository;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;

    public LoggedInViewModel(@NonNull Application application) {
        super(application);

        authAppRepository = new AuthenticationRepository(application);
        userLiveData = authAppRepository.getUserLiveData();
        loggedOutLiveData = authAppRepository.getLoggedOutLiveData();
    }

    public void logOut() {
        authAppRepository.logOut();
    }

    public LiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
}