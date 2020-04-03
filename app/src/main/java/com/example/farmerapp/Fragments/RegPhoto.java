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
public class RegPhoto extends Fragment {

    ImageView img;
    Button next;
    FloatingActionButton add;

    public RegPhoto() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V =  inflater.inflate(R.layout.fragment_reg_photo, container, false);

        next = getView().findViewById(R.id.next);
        img = getView().findViewById(R.id.photo);
        add = getView().findViewById(R.id.upload);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open photo picker via intent
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setting the data in the backend
                RegisterDetailsActivity.scrollPager(1);
            }
        });

        return V;

    }
}
