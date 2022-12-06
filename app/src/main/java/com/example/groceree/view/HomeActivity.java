package com.example.groceree.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.groceree.R;
import com.example.groceree.model.Product;
import com.example.groceree.model.UserModel;
import com.example.groceree.viewmodel.LoggedInViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView nav_view;
    androidx.appcompat.widget.Toolbar toolbar;

    ImageButton btnOpenProfile;
    ImageView ivProfilepic;
    TextView tvName, tvPhone;

    String uid;
    Uri uri;
    String downloadUrl;

    FirebaseDatabase db;
    DatabaseReference node;
//    FirebaseStorage storage;
//    StorageReference uploader;



//    ActionBarDrawerToggle mDrawerToggle;
    RecyclerView recyclerView;

    ArrayList<Product> products = new ArrayList<>();
    LoggedInViewModel loggedInViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        // open update profile page by clicking on right arrow on nav header.




        drawerLayout = findViewById(R.id.drawer_layout);
        nav_view = findViewById(R.id.nv);
        toolbar = findViewById(R.id.toolbar);

        //Navigation drawer menu
        nav_view.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

//      for back pressing activity
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

//      itemclick listner
        nav_view.setNavigationItemSelectedListener(this);


        // profile data fetching
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        // creating object to get instance of whole database
        db = FirebaseDatabase.getInstance();

        // getting reference of a node from database
        node = db.getReference().child("Users").child(uid);
        node.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel user = dataSnapshot.getValue(UserModel.class);

                tvName = findViewById(R.id.tv_user_name_nav_header);
                tvPhone = findViewById(R.id.tv_phone_nav_header);
                ivProfilepic = findViewById(R.id.icv_profile_pic_nav_header);

                ((ImageButton) findViewById(R.id.right_arrow_nav_header)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                    }
                });

                (tvName).setText(""+user.getFullName());
                (tvPhone).setText(""+user.getMobileNumber());
                //tvPhone.setText(""+user.getMobileNumber());

                // fetching image from firebase
                Glide.with(getApplicationContext()).load(user.getProfilePic()).placeholder(R.mipmap.ic_launcher).into( (ivProfilepic) );
                Toast.makeText(getApplicationContext(), "Hello " + user.getFullName() + " !", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });










        products.add(new Product("Cabbabe", "https://backend.lassana.com/images//products/Cabbage--1584342902.jpg", "50Rs."));
        products.add(new Product("Garlic", "https://cdn.britannica.com/37/174537-050-950EE618/Bulbs-cloves-garlic.jpg", "40Rs."));
        products.add(new Product("Onion", "https://www.jiomart.com/images/product/600x600/590002136/onion-5-kg-pack-product-images-o590002136-p590002136-0-202203141906.jpg", "40Rs."));
        products.add(new Product("Potato", "https://www.macmillandictionary.com/external/slideshow/full/141151_full.jpg", "20Rs."));
        products.add(new Product("Tomato", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS3nLWXJMHpL8XmPcOStVLacb2gorGmBybsQOogIOXTL04GCPrJMBGl9Q&s", "30Rs."));

        recyclerView = findViewById(R.id.rv_products_item_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ProductListAdapter productListAdapter = new ProductListAdapter(products);
        recyclerView.setAdapter(productListAdapter);
        productListAdapter.notifyDataSetChanged();

         loggedInViewModel = new ViewModelProvider(this).get(LoggedInViewModel.class);




    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.account:
                Toast.makeText(this, "selected account", Toast.LENGTH_SHORT).show();
                break;

            case R.id.settings:
                Toast.makeText(this, "selected settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mycart:
                Toast.makeText(this, "selected mycart", Toast.LENGTH_SHORT).show();
                break;

        }



        return true;
    }
}






