package com.example.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.farmerapp.Adapters.RegisterDetailsAdapter;
import com.example.farmerapp.R;
import com.example.farmerapp.Retrofit.Farmer;
import com.example.farmerapp.ViewModels.RegisterDetailsViewModel;
import com.example.farmerapp.ViewPager.RegistrationViewPager;
import com.google.android.material.snackbar.Snackbar;

import pl.droidsonroids.gif.GifImageView;

public class RegisterDetailsActivity extends AppCompatActivity {
    RegisterDetailsAdapter adapter;
    public static RegistrationViewPager vPager;
    public static ConstraintLayout parent;
    public static RegisterDetailsViewModel viewModel;
    public static GifImageView load;
    //we can't register farmer details when image is uploading because we need imageURL
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);
        load=findViewById(R.id.load);
        parent=findViewById(R.id.registration_parent);
        vPager = findViewById(R.id.regTabs);
        vPager.disableScroll(true);
        adapter = new RegisterDetailsAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vPager.setAdapter(adapter);

        viewModel= ViewModelProviders.of(this).get(RegisterDetailsViewModel.class);
    }

    public static void scrollPager(int index) {
        vPager.setCurrentItem(index,true);
    }

}
