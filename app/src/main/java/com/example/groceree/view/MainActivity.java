package com.example.groceree.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.groceree.R;
import com.example.groceree.databinding.ActivityMainBinding;
import com.example.groceree.databinding.ActivitySignupBinding;
import com.example.groceree.repository.AuthenticationRepository;
import com.example.groceree.viewmodel.LoggedInViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    LoggedInViewModel loggedInViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnSampleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });









}






    @Override
    public void onStart() {
        super.onStart();






        loggedInViewModel =new ViewModelProvider(this)
                .get(LoggedInViewModel.class);

        loggedInViewModel.getUserLiveData()
                .observe(this, new Observer<FirebaseUser>() {
                    @Override
                    public void onChanged (FirebaseUser firebaseUser){

                        if (firebaseUser != null) {
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            //Toast.makeText(MainActivity.this, "User: " + FirebaseAuth.getInstance().getCurrentUser().getUid() + "logged in", Toast.LENGTH_SHORT).show();
                            finish();
                        }


                    }
                });

        loggedInViewModel.getLoggedOutLiveData()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean loggedOut) {
                        if(loggedOut) {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            Toast.makeText(MainActivity.this, "logged out", Toast.LENGTH_SHORT).show();

                            finish();
                        }
                    }
                });










/*
        AuthenticationRepository authenticationRepository = new AuthenticationRepository((Application) getApplicationContext());
        FirebaseUser firebaseUser = authenticationRepository.getUserLiveData().getValue();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            Toast.makeText(MainActivity.this, "User: " + FirebaseAuth.getInstance().getCurrentUser().getUid() +"logged in", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            Toast.makeText(MainActivity.this, "no user"+ FirebaseAuth.getInstance().getCurrentUser(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));


            finish();
    }

 */



}
}