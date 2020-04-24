package com.farmerapp.Fragments.MainFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farmerapp.Activities.MainActivity;
import com.example.farmerapp.R;

public class PaymentFragment extends Fragment {
    public PaymentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_payment, container, false);
        MainActivity.hideLocation();
        MainActivity.setTitle(getResources().getString(R.string.payments));
        return v;
    }
}
