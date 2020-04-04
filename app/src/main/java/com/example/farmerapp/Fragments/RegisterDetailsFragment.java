package com.example.farmerapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.farmerapp.Activities.RegisterDetailsActivity;
import com.example.farmerapp.R;

public class RegisterDetailsFragment extends Fragment {
    public RegisterDetailsFragment() {
        // Required empty public constructor
    }
    EditText name,alternatePhone;
    Button next;
    ImageView back;
    boolean isnameok=false,isphoneok=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_reg_details, container, false);
        name=v.findViewById(R.id.name);
        alternatePhone=v.findViewById(R.id.phone);
        next=v.findViewById(R.id.next);
        back=v.findViewById(R.id.back);
        addTextListeners();
        addButtonClickListeners();
        return v;
    }
    public void addTextListeners(){
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    isnameok=true;
                }else{
                    isnameok=false;
                }
                updateNextButtonStatus();
            }
        });
        alternatePhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==10){
                    isphoneok=true;
                }else{
                    isphoneok=false;
                }
                updateNextButtonStatus();
            }
        });
    }
    public void addButtonClickListeners(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegisterDetailsActivity)getActivity()).scrollPager(0);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterDetailsActivity.details.setName(name.getText().toString());
                RegisterDetailsActivity.details.setMob(alternatePhone.getText().toString());
                ((RegisterDetailsActivity)getActivity()).scrollPager(2);
            }
        });
    }
    public void updateNextButtonStatus(){
        if(isnameok && isphoneok){
            next.setEnabled(true);
            next.setAlpha(1f);
        }else{
            next.setEnabled(false);
            next.setAlpha(0.3f);
        }
    }

}
