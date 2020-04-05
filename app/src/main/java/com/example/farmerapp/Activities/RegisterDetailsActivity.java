package com.example.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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

public class RegisterDetailsActivity extends AppCompatActivity {
    RegisterDetailsAdapter mAdapter;
    public static RegistrationViewPager vPager;
    public static ConstraintLayout parent;
    public static RegisterDetailsViewModel viewModel;
    //we can't register farmer details when image is uploading because we need imageURL
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);
        parent=findViewById(R.id.registration_parent);
        vPager = findViewById(R.id.regTabs);
        vPager.disableScroll(true);
        mAdapter = new RegisterDetailsAdapter(getSupportFragmentManager());
        viewModel= ViewModelProviders.of(this).get(RegisterDetailsViewModel.class);
        vPager.setAdapter(mAdapter);
        checkifUploadDone();
    }

    public static void scrollPager(int index) {
        vPager.setCurrentItem(index,true);
    }

    public void checkifUploadDone(){
        viewModel.getUploadStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer==-1)
                    showErrorSnackBar();
                else if(integer==1){
                    Intent intent=new Intent(getApplicationContext(),SelectCropActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    public void showErrorSnackBar(){
        Snackbar snackbar=Snackbar.make(RegisterDetailsActivity.parent,"Something Went wrong",Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
