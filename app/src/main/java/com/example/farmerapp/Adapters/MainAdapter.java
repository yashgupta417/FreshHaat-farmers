package com.example.farmerapp.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.farmerapp.Fragments.MainFragments.HomeFragment;
import com.example.farmerapp.Fragments.MainFragments.PaymentFragment;
import com.example.farmerapp.Fragments.MainFragments.RequestFragment;


public class MainAdapter extends FragmentStatePagerAdapter {
    public MainAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        switch(position){
            case 0: fragment = new HomeFragment();break;
            case 1: fragment = new RequestFragment();break;
            case 2: fragment = new PaymentFragment();break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
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
        }
        return null;
    }
}
