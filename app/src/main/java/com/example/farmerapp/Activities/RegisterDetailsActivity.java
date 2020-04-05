package com.example.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.net.Uri;
import android.os.Bundle;

import com.example.farmerapp.Adapters.RegisterDetailsAdapter;
import com.example.farmerapp.R;
import com.example.farmerapp.Retrofit.Farmer;
import com.example.farmerapp.ViewPager.RegistrationViewPager;

public class RegisterDetailsActivity extends AppCompatActivity {
    RegisterDetailsAdapter mAdapter;
    public static RegistrationViewPager vPager;
    public static ConstraintLayout parent;
    public static Uri image;
    public static Farmer farmer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);
        parent=findViewById(R.id.registration_parent);
        farmer = new Farmer();
        vPager = findViewById(R.id.regTabs);
        vPager.disableScroll(true);
        mAdapter = new RegisterDetailsAdapter(getSupportFragmentManager());
        vPager.setAdapter(mAdapter);
    }

    public static void scrollPager(int index) {
        vPager.setCurrentItem(index,true);
    }

}
