package com.example.farmerapp.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.farmerapp.Fragments.RegisterAddress1Fragment;
import com.example.farmerapp.Fragments.RegisterAddress2Fragment;
import com.example.farmerapp.Fragments.RegisterDetailsFragment;
import com.example.farmerapp.Fragments.RegisterPhotoFragment;

public class RegisterDetailsAdapter extends FragmentStatePagerAdapter {
    public RegisterDetailsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        switch(position){
            case 0: fragment = new RegisterPhotoFragment();break;
            case 1: fragment = new RegisterDetailsFragment();break;
            case 2: fragment = new RegisterAddress1Fragment();break;
            case 3: fragment = new RegisterAddress2Fragment();break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Fragment 1";
            case 1:
                return "Fragment 2";
            case 2:
                return "Fragment 3";
            case 3:
                return "Fragment 4";
        }
        return null;
    }
}
