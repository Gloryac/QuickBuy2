package com.example.quickbuy2.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quickbuy2.Activity.LoginActivity;
import com.example.quickbuy2.R;
//import com.example.quickbuy2.Onboarding;


public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN = 5000;

    //animation variables
    Animation sideAnim;
    Animation bottomAnim;


    TextView logo,slogan;

    SharedPreferences OnBoardingScreen;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Animation
        sideAnim = AnimationUtils.loadAnimation(this,R.anim.side_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Hooks

        slogan = findViewById(R.id.textView2);
        logo=findViewById(R.id.textView);

        //set Animation on elements
        logo.setAnimation(sideAnim);
        slogan.setAnimation(bottomAnim);


        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          OnBoardingScreen = getSharedPreferences("OnBoardingScreen",MODE_PRIVATE);
                                          boolean isFirstTime = OnBoardingScreen.getBoolean("firstTime",true);

                                          SharedPreferences.Editor editor= OnBoardingScreen.edit();
                                          editor.putBoolean("firstTime",true);
                                          editor.apply();

                                          if(isFirstTime){
                                              editor = OnBoardingScreen.edit();
                                              editor.putBoolean("firstTime",false);
                                              editor.apply();
                                              Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                                              startActivity(intent);
                                              finish();
                                          } else{
                                              Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                              startActivity(intent);
                                              finish();
                                          }
                                      }
                                  }, //Pass time here
                SPLASH_SCREEN);
    }
}