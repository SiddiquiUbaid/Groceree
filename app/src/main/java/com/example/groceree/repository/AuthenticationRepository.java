package com.example.groceree.repository;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.groceree.model.UserModel;
import com.example.groceree.view.LoginActivity;
import com.example.groceree.view.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class AuthenticationRepository {

    private Application application;

   // private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;

    private UserModel mUser;

    String mMobileNumber, mOtp;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    FirebaseAuth auth;
    String verificationCode;

    public AuthenticationRepository(Application application) {
        this.application = application;
        this.auth = FirebaseAuth.getInstance();
        this.userLiveData = new MutableLiveData<>();
        this.loggedOutLiveData = new MutableLiveData<>();

        if (auth.getCurrentUser() != null) {
            userLiveData.postValue(auth.getCurrentUser());
            loggedOutLiveData.postValue(false);
        }
        else{
            userLiveData.postValue(auth.getCurrentUser());
            loggedOutLiveData.postValue(true);
        }


    }





    // method to generate otp
    public void generateOtp(String mobileNumber, Activity activity){
        mMobileNumber = mobileNumber;

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobileNumber,                     // Phone number to verify
                60,                           // Timeout duration
                TimeUnit.SECONDS,                // Unit of timeout
                activity,        // Activity (for callback binding)
                mCallback);                      // OnVerificationStateChangedCallbacks


    }

    // call on login or sign up button Clicked
    public void onLoginButtonClick(String otp){

        mOtp = otp;
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
        SigninWithPhone(credential);


    }


    // Sign in with phone method
    public void SigninWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            application.startActivity(new Intent(application, MainActivity.class));
                            Toast.makeText(application," OTP  Successfully",Toast.LENGTH_SHORT).show();
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            storeUserDataOnFirebase(uid, mUser);
                            //finish();
                        } else {
                            Toast.makeText(application,"Incorrect OTP",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }




    // Start firebase phone login
    public void StartFirebaseLogin() {

        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(application,"verification completed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(application,"verification fialed"+ e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(application,"Code sent",Toast.LENGTH_SHORT).show();
            }
        };
    }


    public void storeUserDataOnFirebase(String uid, UserModel user){


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users")
                .child(uid);

        // start a progress dialog

        myRef.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(application,  "User registered successfully!", Toast.LENGTH_SHORT).show();
                // end the progress dialog
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(application,  "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }


    public void logOut() {
        auth.signOut();
        loggedOutLiveData.postValue(true);
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }








}
