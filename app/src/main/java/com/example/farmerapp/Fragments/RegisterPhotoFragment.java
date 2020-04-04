package com.example.farmerapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.farmerapp.Activities.RegisterDetailsActivity;
import com.example.farmerapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterPhotoFragment extends Fragment {
    public RegisterPhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=  inflater.inflate(R.layout.fragment_reg_photo, container, false);
        return v;

    }
}
