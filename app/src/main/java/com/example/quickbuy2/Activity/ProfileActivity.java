package com.example.quickbuy2.Activity;

import static com.example.quickbuy2.R.id.bottom_cart;
import static com.example.quickbuy2.R.id.bottom_profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickbuy2.Helper.ReadWriteUserDetails;
import com.example.quickbuy2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.R.id;

public class ProfileActivity extends AppCompatActivity {
    private TextView textViewWelcome,textViewName, textViewEmail, textViewMobile;
    private ProgressBar progressBar;
    private String fullName, email, mobile;
    private ImageView imageView;
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.my_toolbar);  // Replace 'my_toolbar' with your actual ID

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Profile");
        textViewName= findViewById(R.id.nameProfile);
        textViewEmail=findViewById(R.id.emailProfile);
        textViewMobile=findViewById(R.id.phoneProfile);
        textViewWelcome=findViewById(R.id.textViewProfile);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        if(firebaseUser==null){
            Toast.makeText(ProfileActivity.this,"User Details not available at the moment", Toast.LENGTH_LONG).show();
        }else {
            //progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(bottom_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == bottom_profile) {
                Log.d("ProfileActivity", "Profile tab clicked!");
                return true;
            } else if (itemId == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.bottom_cart) {
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
                //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                return true;
            /*} else if (itemId == bottom_settings) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                return true;*/
            } else {
                return false;
            }
        });

    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        //Extracting reference for registered user
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //progressBar.setVisibility(View.VISIBLE);
                ReadWriteUserDetails readUserDetails=snapshot.getValue(ReadWriteUserDetails.class);
                if(readUserDetails!=null){
                    fullName = firebaseUser.getDisplayName();
                    email=firebaseUser.getEmail();
                    mobile=readUserDetails.mobile;
                    textViewWelcome.setText("Welcome"+fullName+"!");
                    textViewName.setText(fullName);
                    textViewEmail.setText(email);
                    textViewMobile.setText(mobile);
                }else {
                    Toast.makeText(ProfileActivity.this, "User details not found", Toast.LENGTH_LONG).show();
                }
                //progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                //progressBar.setVisibility(View.GONE);
            }
        });
    }
}