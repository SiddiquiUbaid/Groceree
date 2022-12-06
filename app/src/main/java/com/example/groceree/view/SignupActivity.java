package com.example.groceree.view;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.groceree.R;
import com.example.groceree.databinding.ActivitySignupBinding;
import com.example.groceree.model.UserModel;
import com.example.groceree.repository.AuthenticationRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class SignupActivity extends AppCompatActivity {

    String profilePic, fullName, dob, pinCode, mobileNumber, password, rePassword, otp;
    ActivitySignupBinding binding;
    private FirebaseAuth auth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    String verificationCode;
    UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);




        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                profilePic = "profile pic url ";
                fullName = binding.etFullname.getText().toString();
                pinCode = binding.etPincode.getText().toString();
                dob = binding.etDob.getText().toString();
                mobileNumber = binding.etSignupMobileNumber.getText().toString();
                password = binding.etSignupPassword.getText().toString();
                rePassword = binding.etSignupRePassword.getText().toString();


                 user = new UserModel(profilePic, fullName, dob, pinCode, mobileNumber, password, rePassword);
                StartFirebaseLogin();
                generateOtp(user.getMobileNumber(), SignupActivity.this);



// magic starts from here...



                //intent.putExtra("user", user);

                binding.btnSignup.setVisibility(View.GONE);
                binding.etConfirmOTP.setVisibility(View.VISIBLE);
                binding.btnConfirmUserRegistrationWithOTP.setVisibility(View.VISIBLE);
                binding.btnConfirmUserRegistrationWithOTP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        otp = binding.etConfirmOTP.getText().toString();
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
                        SigninWithPhone(credential);
                    }
                });







                Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
               // startActivity(intent);








/*
                AuthenticationRepository authRepo = new AuthenticationRepository((Application) getApplicationContext());

                authRepo.storeUserDataOnFirebase(user);

                // Write a message to the database

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Users")
                        .child(mobileNumber);

                // start a progress dialog

                myRef.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),  "User registered successfully!", Toast.LENGTH_SHORT).show();
                        // end the progress dialog
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),  "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                 */




            }
        });




    }


    public void generateOtp(String mobileNumber, Activity activity){


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobileNumber,                     // Phone number to verify
                60,                           // Timeout duration
                TimeUnit.SECONDS,                // Unit of timeout
                activity,        // Activity (for callback binding)
                mCallback);                      // OnVerificationStateChangedCallbacks


    }

    public void storeUserDataOnFirebase(String uid, UserModel user){


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users")
                .child(uid);

        // start a progress dialog

        myRef.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(SignupActivity.this,  "User registered successfully!", Toast.LENGTH_SHORT).show();
                // end the progress dialog
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignupActivity.this,  "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }


    public void SigninWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            Toast.makeText(SignupActivity.this," OTP  Successfully",Toast.LENGTH_SHORT).show();
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            storeUserDataOnFirebase(uid, user);
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(SignupActivity.this,"Incorrect OTP",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void StartFirebaseLogin() {

        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(SignupActivity.this,"verification completed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(SignupActivity.this,"verification fialed"+ e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(SignupActivity.this,"Code sent",Toast.LENGTH_SHORT).show();
            }
        };
    }





}