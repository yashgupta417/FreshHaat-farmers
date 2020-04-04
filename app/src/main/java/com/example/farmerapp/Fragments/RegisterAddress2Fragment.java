package com.example.farmerapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.farmerapp.Activities.RegisterDetailsActivity;
import com.example.farmerapp.R;

public class RegisterAddress2Fragment extends Fragment {
    public RegisterAddress2Fragment() {
        // Required empty public constructor
    }
    ImageView back;
    Button next;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_reg_address2, container, false);
        back=v.findViewById(R.id.back);
        next=v.findViewById(R.id.next);
        addButtonClickListeners();
        return v;
    }
    public void addButtonClickListeners(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegisterDetailsActivity)getActivity()).scrollPager(2);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Upload Details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
