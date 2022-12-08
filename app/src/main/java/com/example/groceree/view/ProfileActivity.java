package com.example.groceree.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.groceree.R;
import com.example.groceree.databinding.ActivityProfileBinding;
import com.example.groceree.databinding.ActivitySignupBinding;
import com.example.groceree.model.UserModel;
import com.example.groceree.viewmodel.LoggedInViewModel;
import com.example.groceree.viewmodel.ProfileViewModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    String profilePic, fullName, dob, pinCode, mobileNumber, address;
    UserModel userUpdatedData;
    ProfileViewModel profileViewModel;
    private Uri uri;


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

                // assigning profilePic url from observer to global variable profilePic
                // this variable will be passed directly into UserModel constructor
                // when update button is clicked to update overall profile
                profilePic = userModel.getProfilePic();

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


        // logic for close button
        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // logic for help button
        binding.btnHelpProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // assist user by adding dialog box to update his profile data and image
                Toast.makeText(ProfileActivity.this, "Click on a field to update", Toast.LENGTH_SHORT).show();
            }
        });


        // clicking on profile image to update it
        binding.icvProfilePicUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagePicker.with(ProfileActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(480)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(480, 480)     //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(1);


            }
        });



        // clicking on update button to update profile with new values
        binding.btnProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //profilePic = "profile pic url ";
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

    // getting result and uri from ImagePicker and passing it to uploadImage()
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            uri = data.getData();

            binding.icvProfilePicUpdate.setImageURI(uri);
            uploadImage(uri); // ubaid code


        }
    }


    // UploadImage method
    // accepts Uri and uploads image on firebase Storage
    // then generates downloadable url
    // and stores the url on realtime db for further references.
    public void uploadImage(Uri uri) {
        if (uri != null) {

            // Code for showing progressDialog while uploading
            binding.profileUpdateProgressSpinner.setVisibility(View.VISIBLE);

            // Defining the child of storageReference
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference uploader = storage.getReference().child(uid);


            // adding listeners on upload
            // or failure of image
            uploader.putFile(uri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // on successful image upload
                                    // getting its downloadable url.
                                    uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            // on successfully getting download url
                                            // store it in realtime firebase by going to current users id.
                                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                                            DatabaseReference node = db.getReference("Users").child(uid);
                                            node.child("profilePic").setValue(uri.toString())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            binding.profileUpdateProgressSpinner.setVisibility(View.GONE);

                                                            // on successfully storing url show toast
                                                            Toast
                                                                    .makeText(getApplicationContext(),
                                                                            "Profile pic updated",
                                                                            Toast.LENGTH_SHORT)
                                                                    .show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            binding.profileUpdateProgressSpinner.setVisibility(View.GONE);

                                                            // on failing to store url show toast
                                                            Toast
                                                                    .makeText(getApplicationContext(),
                                                                            "Update Failed: " + e.getMessage(),
                                                                            Toast.LENGTH_SHORT)
                                                                    .show();
                                                        }
                                                    });


                                        }
                                    });



                                }
                            })

                    // if image uploading failed
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error, Image not uploaded
                            // gone visibility of spinner;
                            binding.profileUpdateProgressSpinner.setVisibility(View.GONE);
                            Toast
                                    .makeText(getApplicationContext(),
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();


                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // for future use
                                // how to show live percentage of transfer on progressDialog
                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                   // progressDialog.setMessage("Updating...");
                                }
                            });
        }

    }




}