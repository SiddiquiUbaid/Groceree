package com.example.groceree.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.groceree.model.UserModel;
import com.example.groceree.repository.AuthenticationRepository;
import com.example.groceree.repository.UserDataRepository;
import com.google.firebase.auth.FirebaseUser;

public class ProfileViewModel extends AndroidViewModel {
    private UserDataRepository userDataRepository;
    private MutableLiveData<UserModel> userUpdatedLiveData;


    public ProfileViewModel(Application application){
        super(application);

        userDataRepository = new UserDataRepository(application);
        userUpdatedLiveData = userDataRepository.getUserDataFromFirebase();

    }

    public void setUserUpdatedLiveData(UserModel user) {
        userDataRepository.updateUserDataOnFirebase(user);

    }
    public LiveData<UserModel> getUserUpdatedLiveData() {
        return userUpdatedLiveData;
    }




}
