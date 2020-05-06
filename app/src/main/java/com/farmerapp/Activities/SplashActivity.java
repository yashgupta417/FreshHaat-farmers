package com.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.farmerapp.R;
import com.farmerapp.Utils.LocalCart;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    public static String IS_LOGGED_IN="is_logged_in";
    public static String IS_REGISTRATION_DONE="is_registration_done";
    public static String TOKEN="token";
    public static String USER_ID="userId";
    public static String MOBILE_NO="mobile_number";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initializeLocalCartCount();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                enter();
            }
        }, 2000);
    }
    public void initializeLocalCartCount(){
        LocalCart.count=LocalCart.getProductIds(getApplication()).size();
    }
    public void enter(){
        SharedPreferences sharedPreferences=getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        Boolean isLoggedIn=sharedPreferences.getBoolean(IS_LOGGED_IN,false);
        Boolean isRegistrationDone=sharedPreferences.getBoolean(IS_REGISTRATION_DONE,false);
        String token=sharedPreferences.getString(TOKEN,null);
        Intent intent;
        if(!isLoggedIn || token==null){
            intent=new Intent(getApplicationContext(),LoginActivity.class);
        }else if(!isRegistrationDone){
            intent=new Intent(getApplicationContext(),RegisterDetailsActivity.class);
        }
        else{
            intent=new Intent(getApplicationContext(),MainActivity.class);
        }
        startActivity(intent);
        LanguageActivity.loadSavedLocale(this);//this will go to language activity too when the app is opened first time
        finish();
    }
}
