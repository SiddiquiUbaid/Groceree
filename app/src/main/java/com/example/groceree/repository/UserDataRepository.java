package com.example.groceree.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.example.groceree.R;
import com.example.groceree.model.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDataRepository {
    private Application application;

    private MutableLiveData<UserModel> updatedUserLiveData;
   // private UserModel mUser;
    String uid;
    FirebaseAuth auth;

    public UserDataRepository(Application application) {
        this.application = application;
        this.auth = FirebaseAuth.getInstance();
        this.uid = auth.getCurrentUser().getUid();
        this.updatedUserLiveData = new MutableLiveData<>();



    }



    public void updateUserDataOnFirebase(UserModel user){


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users")
                .child(uid);

        // start a progress dialog

        myRef.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(application,  "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                // end the progress dialog
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(application,  "Failed updating profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }


    public MutableLiveData<UserModel> getUserDataFromFirebase(){


        // profile data fetching
        //String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // creating object to get instance of whole database
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        // getting reference of a node from database
        DatabaseReference node = db.getReference().child("Users").child(uid);
        node.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserModel user = dataSnapshot.getValue(UserModel.class);
                updatedUserLiveData.postValue(user);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("UserDataRepoError", ""+ error.getMessage());

            }
        });


        return updatedUserLiveData;

    }





}
