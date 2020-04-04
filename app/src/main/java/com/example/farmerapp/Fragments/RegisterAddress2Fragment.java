package com.example.farmerapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.farmerapp.Activities.RegisterDetailsActivity;
import com.example.farmerapp.Adapters.SpinnerAdapter;
import com.example.farmerapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterAddress2Fragment extends Fragment implements AdapterView.OnItemSelectedListener {
    public RegisterAddress2Fragment() {
        // Required empty public constructor
    }
    ImageView back;
    Button next;
    Spinner city,state;
    SpinnerAdapter stateAdapter,cityAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_reg_address2, container, false);
        back=v.findViewById(R.id.back);
        next=v.findViewById(R.id.next);
        state = v.findViewById(R.id.state);
        city = v.findViewById(R.id.city);
        addButtonClickListeners();

        stateAdapter = new SpinnerAdapter(getContext(), new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.india_states))));

        state.setAdapter(stateAdapter);
        state.setGravity(View.TEXT_ALIGNMENT_CENTER);
        state.setOnItemSelectedListener(this);

        cityAdapter = new SpinnerAdapter(getContext(), new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.indian_cities))));
        city.setAdapter(cityAdapter);
        state.setGravity(View.TEXT_ALIGNMENT_CENTER);
        city.setOnItemSelectedListener(this);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        switch (view.getId()){
            case R.id.city:RegisterDetailsActivity.details.setCity(item);break;
            case R.id.state:RegisterDetailsActivity.details.setState(item);break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
