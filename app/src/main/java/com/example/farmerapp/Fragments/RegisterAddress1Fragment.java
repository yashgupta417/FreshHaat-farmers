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

public class RegisterAddress1Fragment extends Fragment {
    public RegisterAddress1Fragment() {
        // Required empty public constructor
    }
    EditText address1,address2,landmark;
    ImageView back;
    Button next;
    Boolean isAddress1Ok=false,isAddress2OK=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_reg_address1, container, false);
        address1=v.findViewById(R.id.address_1);
        address2=v.findViewById(R.id.address_2);
        landmark=v.findViewById(R.id.landmark);
        back=v.findViewById(R.id.back);
        next=v.findViewById(R.id.next);
        addTextListeners();
        addButtonClickListeners();
        return v;
    }
    public void addTextListeners(){
        address1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    isAddress1Ok=true;
                }else{
                    isAddress1Ok=false;
                }
                updateNextButtonStatus();
            }
        });
        address2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    isAddress2OK=true;
                }else{
                    isAddress2OK=false;
                }
                updateNextButtonStatus();
            }
        });
    }
    public void addButtonClickListeners(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegisterDetailsActivity)getActivity()).scrollPager(1);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegisterDetailsActivity)getActivity()).scrollPager(3);
            }
        });
    }
    public void updateNextButtonStatus(){
        if(isAddress1Ok && isAddress2OK){
            next.setEnabled(true);
            next.setAlpha(1);
        }else{
            next.setEnabled(false);
            next.setAlpha(0.3f);
        }
    }

}
