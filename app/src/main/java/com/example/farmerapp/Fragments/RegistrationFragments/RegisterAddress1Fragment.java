package com.example.farmerapp.Fragments.RegistrationFragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmerapp.Activities.RegisterDetailsActivity;
import com.example.farmerapp.R;
import com.example.farmerapp.Utils.LocationUtil;
import com.example.farmerapp.ViewModels.RegisterDetailsViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.farmerapp.Activities.RegisterDetailsActivity.viewModel;

public class RegisterAddress1Fragment extends Fragment {



    EditText address1, address2, landmark;
    ImageView back;
    LinearLayout locationLL;
    Button next;
    Boolean isAddress1Ok = false, isAddress2OK = false;
    TextView locationTextView;
    Integer locationStatus=0;
    public RegisterAddress1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reg_address1, container, false);
        address1 = v.findViewById(R.id.address_1);
        address2 = v.findViewById(R.id.address_2);
        landmark = v.findViewById(R.id.landmark);
        locationLL = v.findViewById(R.id.location_ll);
        back = v.findViewById(R.id.back);
        next = v.findViewById(R.id.next);
        locationTextView=v.findViewById(R.id.location_textview);
        addTextListeners();
        addButtonClickListeners();
        return v;
    }


    public void addTextListeners() {
        address1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    isAddress1Ok = true;
                } else {
                    isAddress1Ok = false;
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
                if (s.length() > 0) {
                    isAddress2OK = true;
                } else {
                    isAddress2OK = false;
                }
                updateNextButtonStatus();
            }
        });
    }

    public void addButtonClickListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegisterDetailsActivity) getActivity()).scrollPager(1);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegisterDetailsActivity) getActivity()).scrollPager(3);
            }
        });
        locationLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermissionAndGetAddress();//it will get pin,state,city as well and will be saved but address2 will not be skipped
            }
        });
    }

    public void updateNextButtonStatus() {
        if (isAddress1Ok) {//Address 1 is enough
            next.setEnabled(true);
            next.setAlpha(1);
            String address=address1.getText().toString()+" "+address2.getText().toString()+" "+landmark.getText().toString();
            viewModel.farmer.setAddress(address);
        } else {
            next.setEnabled(false);
            next.setAlpha(0.3f);
        }
    }
    public void getAddress(){
        LocationUtil locationUtil=new LocationUtil(getActivity().getApplication());
        if(!locationUtil.isLocationEnabled(getContext())){
            Snackbar snackbar=Snackbar.make(RegisterDetailsActivity.parent,"Please turn your GPS on.",Snackbar.LENGTH_SHORT);
            snackbar.show();
            return;
        }
        if(locationStatus==1)
            return;//location already fetched
        locationTextView.setText("Locating....");
        locationUtil.observeAddress().observe(this, new Observer<List<Address>>() {
            @Override
            public void onChanged(List<Address> addresses) {
                String add1 = addresses.get(0).getAddressLine(0);
                String add2 = addresses.size() > 1 ? addresses.get(0).getAddressLine(1) : "";
                String locality = addresses.get(0).getLocality();
                String subLocality = addresses.get(0).getSubLocality();
                String state = addresses.get(0).getAdminArea();
                String postalCode = addresses.get(0).getPostalCode();
                updateAddressInUI(add1,subLocality,state,postalCode);
                locationStatus=1;
            }
        });

    }
    public void updateAddressInUI(String add1,String subLocality,String state,String postalCode){
        address1.setText(add1);
        address2.setText(subLocality+" "+state+" "+postalCode);
        locationTextView.setText(subLocality+","+state);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getAddress();
            }
        }
    }

    private void getPermissionAndGetAddress() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
            } else {
                getAddress();
            }
        } else {
            getAddress();
        }
    }

}
