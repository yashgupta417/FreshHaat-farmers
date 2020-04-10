package com.example.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.farmerapp.R;

public class SplashActivity extends AppCompatActivity {
    public static String IS_LOGGED_IN="is_logged_in";
    public static String IS_REGISTRATION_DONE="is_registration_done";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                enter();
            }
        }, 2000);
    }
    public void enter(){
        SharedPreferences sharedPreferences=getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        Boolean isLoggedIn=sharedPreferences.getBoolean(IS_LOGGED_IN,false);
        Boolean isRegistrationDone=sharedPreferences.getBoolean(IS_REGISTRATION_DONE,false);
        Intent intent;
        if(!isLoggedIn){
            intent=new Intent(getApplicationContext(),LoginActivity.class);
        }else if(!isRegistrationDone){
            intent=new Intent(getApplicationContext(),RegisterDetailsActivity.class);
        }
        else{
            intent=new Intent(getApplicationContext(),MainActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
