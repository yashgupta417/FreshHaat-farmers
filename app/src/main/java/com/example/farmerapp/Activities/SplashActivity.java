package com.example.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.farmerapp.R;

public class SplashActivity extends AppCompatActivity {

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
        Boolean isLoggedIn=sharedPreferences.getBoolean("is_logged_in",false);
        Boolean isRegistrationDone=sharedPreferences.getBoolean("is_registration_done",false);
        Boolean cropsSelected=sharedPreferences.getBoolean("crops_selected",false);
        Intent intent;
        if(!isLoggedIn){
            intent=new Intent(getApplicationContext(),LoginActivity.class);
        }else if(!isRegistrationDone){
            intent=new Intent(getApplicationContext(),RegisterDetailsActivity.class);
        }
        else  if(!cropsSelected){
            intent=new Intent(getApplicationContext(),SelectCropActivity.class);
        }
        else{
            intent=new Intent(getApplicationContext(),HomeActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
