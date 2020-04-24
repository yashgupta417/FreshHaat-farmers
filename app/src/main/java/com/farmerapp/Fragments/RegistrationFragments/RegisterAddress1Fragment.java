package com.farmerapp.Fragments.RegistrationFragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.text.Editable;
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

import com.farmerapp.Activities.RegisterDetailsActivity;
import com.example.farmerapp.R;
import com.farmerapp.Utils.LocationUtil;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                if (s.toString().trim().length() > 0) {
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
                if (s.toString().trim().length() > 0) {
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
                RegisterDetailsActivity.viewModel.farmer.setAddressLine1(address1.getText().toString());
                RegisterDetailsActivity.viewModel.farmer.setAddressLine2(address2.getText().toString());
                RegisterDetailsActivity.viewModel.farmer.setLandmark(landmark.getText().toString());
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
        } else {
            next.setEnabled(false);
            next.setAlpha(0.3f);
        }
    }
    public void getAddress(){
        LocationUtil locationUtil=new LocationUtil(getActivity().getApplication());
        if(!locationUtil.isProviderEnabled()){
            Snackbar snackbar=Snackbar.make(RegisterDetailsActivity.parent,"Please turn your GPS on",Snackbar.LENGTH_SHORT);
            snackbar.show();
            return;
        }
        locationTextView.setText(getActivity().getResources().getString(R.string.locating));
        locationUtil.observeAddress().observe(this, new Observer<List<Address>>() {
            @Override
            public void onChanged(List<Address> addresses) {
                extractAddressInfo(addresses);
                locationStatus=1;
            }
        });

    }
    public void extractAddressInfo(List<Address> addresses){
        String add1 =addresses.get(0).getAddressLine(0);
        String locality = addresses.get(0).getLocality();
        String subLocality = addresses.get(0).getSubLocality();
        String subAdminArea=addresses.get(0).getSubAdminArea();
        String adminArea = addresses.get(0).getAdminArea();
        String postalCode = addresses.get(0).getPostalCode();
        String header="",add2="";
        if(subAdminArea!=null)
            add2+=subAdminArea+" ";
        if(adminArea!=null)
            add2+=adminArea;
        if(subLocality!=null)
            header+=subLocality;
        if(locality!=null){
            if(header.length()>0)
                header+=",";
            header+=locality;
        }


        //For using in next fragment
        if(postalCode!=null) {
            RegisterDetailsActivity.viewModel.farmer.setPin(postalCode);
            Log.i("adddd", RegisterDetailsActivity.viewModel.farmer.getPin());
        }
        ArrayList<String> states=new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.india_states)));
        for(String s:states){
            if(adminArea!=null && s.toLowerCase().equals(adminArea.toLowerCase())){
                RegisterDetailsActivity.viewModel.stateIndex=states.indexOf(s);
                break;
            }
        }
        ArrayList<String> cities=new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.indian_cities)));
        for(String c:cities){
            if(locality!=null && c.toLowerCase().equals(locality.toLowerCase())){
                RegisterDetailsActivity.viewModel.cityIndex=cities.indexOf(c);
                break;
            }
        }
        updateAddressInUI(add1,add2,header);
        RegisterAddress2Fragment.updateAddressDetails();
    }
    public void updateAddressInUI(String add1,String add2,String header){
        if(add1!=null)
            address1.setText(add1);
        if(add2!=null)
            address2.setText(add2);
        if(header!=null)
            locationTextView.setText(header);
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
