package com.example.farmerapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.farmerapp.Adapters.RegisterDetailsAdapter;
import com.example.farmerapp.Data.FarmerDetails;
import com.example.farmerapp.R;
import com.example.farmerapp.ViewPager.RegistrationViewPager;

public class RegisterDetailsActivity extends AppCompatActivity {
    RegisterDetailsAdapter mAdapter;
    static RegistrationViewPager vPager;
    public static FarmerDetails details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);
        details = new FarmerDetails("");
        vPager = findViewById(R.id.regTabs);
        vPager.disableScroll(true);
        mAdapter = new RegisterDetailsAdapter(getSupportFragmentManager());
        vPager.setAdapter(mAdapter);
    }

    public static void scrollPager(int index) {
        vPager.setCurrentItem(index,true);
    }

}
