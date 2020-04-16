package com.example.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.example.farmerapp.Adapters.RegisterDetailsAdapter;
import com.example.farmerapp.R;
import com.example.farmerapp.ViewModels.RegisterDetailsViewModel;
import com.example.farmerapp.ViewPager.RegistrationViewPager;

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

    @Override
    public void onBackPressed() {
        if( vPager.getCurrentItem()>0){
            scrollPager(vPager.getCurrentItem()-1);
        }else{
            super.onBackPressed();
        }

    }
}
