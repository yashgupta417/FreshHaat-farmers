package com.farmerapp.Fragments.RegistrationFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.farmerapp.Activities.RegisterDetailsActivity;
import com.farmerapp.Activities.SelectCropActivity;
import com.farmerapp.Adapters.SpinnerAdapter;
import com.example.farmerapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;


public class RegisterAddress2Fragment extends Fragment implements AdapterView.OnItemSelectedListener {
    public RegisterAddress2Fragment() {
        // Required empty public constructor
    }
    ImageView back;
    Button next;
    public static Spinner citySpinner,stateSpinner;
    SpinnerAdapter stateAdapter,cityAdapter;
    public static EditText pincode;
    boolean isPincodeOk=false,isCityOk=false,isStateOk=false;
    ArrayList<String> indianCities,indianStates;
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
        indianStates=new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.india_states)));
        indianCities=new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.indian_cities)));

        stateAdapter = new SpinnerAdapter(getContext(),indianStates);
        stateSpinner.setAdapter(stateAdapter);
        stateSpinner.setOnItemSelectedListener(this);

        cityAdapter = new SpinnerAdapter(getContext(),indianCities);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setOnItemSelectedListener(this);

        return v;

    }

    public static void updateAddressDetails(){
        if(RegisterDetailsActivity.viewModel.farmer.getPin()!=null ) {
            pincode.setText(RegisterDetailsActivity.viewModel.farmer.getPin());
        }
        if(RegisterDetailsActivity.viewModel.cityIndex!=-1)
            citySpinner.setSelection(RegisterDetailsActivity.viewModel.cityIndex);
        if(RegisterDetailsActivity.viewModel.stateIndex!=-1)
            stateSpinner.setSelection(RegisterDetailsActivity.viewModel.stateIndex);
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
                RegisterDetailsActivity.viewModel.registerFarmerDetails();
                updateUI(false,0.3f,View.VISIBLE);
                checkifUploadDone();
            }
        });
        citySpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });
        stateSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });
    }

    public void checkifUploadDone(){
        RegisterDetailsActivity.viewModel.getUploadStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer==-1) {
                    showErrorSnackBar();
                    updateUI(true,1f,View.INVISIBLE);
                }
                else if(integer==1){
                    Intent intent=new Intent(getActivity(), SelectCropActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
    }
    public void updateUI(Boolean bool,Float alpha,int i){
        next.setEnabled(bool);
        next.setAlpha(alpha);
        back.setEnabled(bool);
        back.setAlpha(alpha);
        RegisterDetailsActivity.load.setVisibility(i);
    }
    public void showErrorSnackBar(){
        Snackbar snackbar=Snackbar.make(RegisterDetailsActivity.parent,"Something Went wrong",Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        if(parent.getId()==R.id.city) {
                if (position==0) {
                    isCityOk = false;
                } else {
                    RegisterDetailsActivity.viewModel.farmer.setCity(item);
                    isCityOk = true;
                }
                updateNextButtonStatus();
        }else if(parent.getId()==R.id.state){
            if(position==0) {
                    isStateOk=false;
            }
            else {
                RegisterDetailsActivity.viewModel.farmer.setState(item);
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
                RegisterDetailsActivity.viewModel.farmer.setPin(pincode.getText().toString());
                updateNextButtonStatus();
            }
        });
    }

    public void updateNextButtonStatus(){
        if(isStateOk && isCityOk && isPincodeOk){
            next.setEnabled(true);
            next.setAlpha(1f);
        }else{
            next.setEnabled(false);
            next.setAlpha(0.3f);
        }
    }
    public void hideKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
