package com.farmerapp.Fragments.MainFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.farmerapp.Activities.MainActivity;
import com.example.farmerapp.R;
import com.farmerapp.Activities.PaymentDetailActivity;

public class PaymentFragment extends Fragment {
    public PaymentFragment() {
    }
    Button settings;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_payment, container, false);
        MainActivity.hideLocation();
        MainActivity.setTitle(getResources().getString(R.string.payments));
        settings=v.findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPaymentSettings();
            }
        });
        return v;
    }
    public void goToPaymentSettings(){
        Intent intent=new Intent(getActivity(), PaymentDetailActivity.class);
        getActivity().startActivity(intent);
    }
}
