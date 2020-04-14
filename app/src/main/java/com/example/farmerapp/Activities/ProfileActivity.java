package com.example.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.farmerapp.Data.Farmer;
import com.example.farmerapp.R;
import com.example.farmerapp.ViewModels.ProfileViewModel;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

public class ProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    CircleImageView imageView;
    TextView mobileTextView;
    EditText nameEdittext,alternateMobileEdittext,kycEdittext,addressEdittext;
    ProfileViewModel viewModel;
    ConstraintLayout body;
    GifImageView load;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView=findViewById(R.id.image);
        mobileTextView=findViewById(R.id.mobile_number);
        nameEdittext=findViewById(R.id.name);
        alternateMobileEdittext=findViewById(R.id.alternate_mobile_number);
        kycEdittext=findViewById(R.id.kyc);
        addressEdittext=findViewById(R.id.address);
        load=findViewById(R.id.load);
        body=findViewById(R.id.body_parent);
        handleVisibility(View.VISIBLE,View.GONE);
        viewModel= ViewModelProviders.of(this).get(ProfileViewModel.class);
        viewModel.getFarmer().observe(this, new Observer<Farmer>() {
            @Override
            public void onChanged(Farmer farmer) {
                handleVisibility(View.GONE,View.VISIBLE);
                updateUI(farmer);
            }
        });

    }
    public void handleVisibility(Integer loadVisibility,Integer bodyVisibility){
        load.setVisibility(loadVisibility);
        body.setVisibility(bodyVisibility);
    }
    public void updateUI(Farmer farmer){
        if(farmer.getProfilePhoto()!=null){
            Glide.with(this).load(farmer.getProfilePhoto()).placeholder(R.drawable.load_static).into(imageView);
            imageView.setBorderWidth(5);
            imageView.setBorderColor(getResources().getColor(R.color.colorPrimary));
        }
        SharedPreferences preferences=getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String mobileNumber=preferences.getString(SplashActivity.MOBILE_NO,null);
        mobileTextView.setText(mobileNumber);
        nameEdittext.setText(farmer.getName());
        if(farmer.getAlternateMob()!=null)
            alternateMobileEdittext.setText(farmer.getAlternateMob());
        if(farmer.getKYC()!=null)
            kycEdittext.setText(farmer.getKYC());
        if(farmer.getAddress()!=null){
            addressEdittext.setText(farmer.getAddress());
        }
    }
    public void onBackClick(View view){
        finish();
    }
}
