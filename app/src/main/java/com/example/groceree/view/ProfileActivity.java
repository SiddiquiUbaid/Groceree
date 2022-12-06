package com.example.groceree.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.groceree.R;
import com.example.groceree.databinding.ActivityProfileBinding;
import com.example.groceree.databinding.ActivitySignupBinding;
import com.example.groceree.model.UserModel;
import com.example.groceree.viewmodel.LoggedInViewModel;
import com.example.groceree.viewmodel.ProfileViewModel;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    String profilePic, fullName, dob, pinCode, mobileNumber, address;
    UserModel userUpdatedData;
    ProfileViewModel profileViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);





        profileViewModel =new ViewModelProvider(this)
                .get(ProfileViewModel.class);
        profileViewModel.getUserUpdatedLiveData().observe(this, new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {



                // setting text on edittext
                //binding.icvProfilePicUpdate.setText(userModel.getFullName());
                binding.etFullnameUpdate.setText(userModel.getFullName());
                binding.etFullnameUpdate.requestFocus(View.TEXT_DIRECTION_LTR);

                binding.etMobileNumberUpdate.setText(userModel.getMobileNumber());
                binding.etDobUpdate.setText(userModel.getDob());
                binding.etPincodeUpdate.setText(userModel.getPinCode());
                binding.etAddressUpdate.setText(userModel.getAddress());

                Glide.with(getApplicationContext()).load(userModel.getProfilePic()).placeholder(R.mipmap.ic_launcher).into( (binding.icvProfilePicUpdate) );


            }
        });





        binding.btnProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                profilePic = "profile pic url ";
                fullName = binding.etFullnameUpdate.getText().toString();
                mobileNumber = binding.etMobileNumberUpdate.getText().toString();
                dob = binding.etDobUpdate.getText().toString();
                pinCode = binding.etPincodeUpdate.getText().toString();
                address = binding.etAddressUpdate.getText().toString();

                userUpdatedData = new UserModel(profilePic, fullName, dob, pinCode, mobileNumber, address);
                profileViewModel.setUserUpdatedLiveData(userUpdatedData);



            }
        });







    }
}