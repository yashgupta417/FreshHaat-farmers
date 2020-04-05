package com.example.farmerapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.farmerapp.Activities.RegisterDetailsActivity;
import com.example.farmerapp.Adapters.SpinnerAdapter;
import com.example.farmerapp.R;
import com.example.farmerapp.ViewModels.RegisterDetailsViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.farmerapp.Activities.RegisterDetailsActivity.viewModel;

public class RegisterAddress2Fragment extends Fragment implements AdapterView.OnItemSelectedListener {
    public RegisterAddress2Fragment() {
        // Required empty public constructor
    }
    ImageView back;
    Button next;
    Spinner citySpinner,stateSpinner;
    SpinnerAdapter stateAdapter,cityAdapter;
    EditText pincode;
    boolean isPincodeOk=false,isCityOk=false,isStateOk=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_reg_address2, container, false);
        back=v.findViewById(R.id.back);
        next=v.findViewById(R.id.next);
        stateSpinner = v.findViewById(R.id.state);
        citySpinner = v.findViewById(R.id.city);
        pincode=v.findViewById(R.id.pin);
        addButtonClickListeners();
        addTextListener();

        stateAdapter = new SpinnerAdapter(getContext(), new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.india_states))));
        stateSpinner.setAdapter(stateAdapter);
        stateSpinner.setOnItemSelectedListener(this);

        cityAdapter = new SpinnerAdapter(getContext(), new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.indian_cities))));
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setOnItemSelectedListener(this);

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
                viewModel.registerFarmerDetails();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        if(parent.getId()==R.id.city) {
                if (item.equals("City")) {
                    isCityOk = false;
                } else {
                    viewModel.farmer.setCity(item);
                    isCityOk = true;
                }
                updateNextButtonStatus();
        }else if(parent.getId()==R.id.state){
            if(item.equals("State")) {
                    isStateOk=false;
            }
            else {
                viewModel.farmer.setState(item);
                isStateOk=true;
            }
            updateNextButtonStatus();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void addTextListener() {
        pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==6) {
                    isPincodeOk = true;
                } else {
                    isPincodeOk = false;
                }
                updateNextButtonStatus();
            }
        });
    }

    public void updateNextButtonStatus(){
        if(isStateOk && isCityOk && isPincodeOk){
            next.setEnabled(true);
            next.setAlpha(1f);
            viewModel.farmer.setPin(pincode.getText().toString());//city and state are already saved
        }else{
            next.setEnabled(false);
            next.setAlpha(0.3f);
        }
    }

}
