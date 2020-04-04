package com.example.farmerapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.farmerapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterAddress2Fragment extends Fragment {

    public RegisterAddress2Fragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reg_address2, container, false);
    }
}
