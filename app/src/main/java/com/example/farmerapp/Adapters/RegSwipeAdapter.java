package com.example.farmerapp.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.farmerapp.Fragments.RegAddress1;
import com.example.farmerapp.Fragments.RegAddress2;
import com.example.farmerapp.Fragments.RegDetails;
import com.example.farmerapp.Fragments.RegPhoto;

public class RegSwipeAdapter extends FragmentStatePagerAdapter {
    public RegSwipeAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        switch(position){
            case 0: fragment = new RegPhoto();break;
            case 1: fragment = new RegDetails();break;
            case 2: fragment = new RegAddress1();break;
            case 3: fragment = new RegAddress2();break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
