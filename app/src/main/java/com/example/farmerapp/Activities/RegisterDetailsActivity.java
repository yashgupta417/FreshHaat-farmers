package com.example.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.farmerapp.Adapters.RegSwipeAdapter;
import com.example.farmerapp.R;

public class RegisterDetailsActivity extends AppCompatActivity {
    RegSwipeAdapter mAdapter;
    static ViewPager vPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        vPager = findViewById(R.id.regTabs);
        //vPager.setCurrentItem(0);
        vPager.setAdapter(mAdapter);
        mAdapter = new RegSwipeAdapter(getSupportFragmentManager());

    }

    public static void scrollPager(int index){
        vPager.setCurrentItem(index);
    }
}
