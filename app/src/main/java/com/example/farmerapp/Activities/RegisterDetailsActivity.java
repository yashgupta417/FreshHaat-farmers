package com.example.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.farmerapp.Adapters.RegisterDetailsAdapter;
import com.example.farmerapp.R;

public class RegisterDetailsActivity extends AppCompatActivity {
    RegisterDetailsAdapter mAdapter;
    static ViewPager vPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);
        vPager = findViewById(R.id.regTabs);
        mAdapter = new RegisterDetailsAdapter(getSupportFragmentManager());
        vPager.setAdapter(mAdapter);
    }

    public static void scrollPager(int index) {
        vPager.setCurrentItem(index);
    }
}
