package com.farmerapp.BottomSheets;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.example.farmerapp.R;
import com.farmerapp.Utils.LocationUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddressBottomSheet extends BottomSheetDialogFragment{
    public AddressBottomSheet(com.farmerapp.Data.Address address){
         this.address=address;
    }
    Button confirmLocation;
    LinearLayout locationLL;
    EditText addressLine1,addressLine2,landmark,pincode,city,state;
    TextView currentLocationText;
    RelativeLayout parent;
    public com.farmerapp.Data.Address address;
    Boolean isAddressOk=false,isPincodeOK=false,isCityOk=false,isStateOk=false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_address, container, false);
        confirmLocation=v.findViewById(R.id.confirm_location);
        locationLL=v.findViewById(R.id.location_ll);
        addressLine1=v.findViewById(R.id.address_line_1);
        addressLine2=v.findViewById(R.id.address_line_2);
        landmark=v.findViewById(R.id.landmark);
        pincode=v.findViewById(R.id.pincode);
        city=v.findViewById(R.id.city);
        state=v.findViewById(R.id.state);
        currentLocationText=v.findViewById(R.id.current_location_text);
        parent=v.findViewById(R.id.parent);
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                View bottomSheetInternal = d.findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        confirmLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLocationWork();
                dismiss();
            }
        });
        locationLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermissionAndGetLocation();
            }
        });
        activateListeners();
        initializeUI();
        return v;
    }
    public void initializeUI(){
        if(address==null)
            return;
        if(address.getAddressLine1()!=null)
            addressLine1.setText(address.getAddressLine1());
        if(address.getAddressLine2()!=null)
            addressLine2.setText(address.getAddressLine2());
        if(address.getLandmark()!=null)
            landmark.setText(address.getLandmark());
        if(address.getPin()!=null)
            pincode.setText(address.getPin());
        if(address.getCity()!=null)
            city.setText(address.getCity());
        if(address.getState()!=null)
            state.setText(address.getState());
    }
    private OnConfirmLocationListener mListener;
    public interface OnConfirmLocationListener {
        void onConfirmLocation(com.farmerapp.Data.Address address);
    }

    public void setOnConfirmListener(OnConfirmLocationListener listener){
        mListener=listener;
    }
    public void confirmLocationWork(){
        address.setAddressLine1(addressLine1.getText().toString().trim());
        address.setAddressLine2(addressLine2.getText().toString().trim());
        address.setLandmark(landmark.getText().toString());
        address.setPin(pincode.getText().toString());
        address.setCity(city.getText().toString());
        address.setState(state.getText().toString());
        mListener.onConfirmLocation(address);
    }

    public void getLocation(){
            currentLocationText.setText(getResources().getString(R.string.locating));
            LocationUtil locationUtil=new LocationUtil(getActivity().getApplication());
            if(locationUtil.isProviderEnabled()){
                locationUtil.observeAddress().observe(this, new Observer<List<Address>>() {
                    @Override
                    public void onChanged(List<Address> addresses) {
                        extractAddressInfoAndShowInUI(addresses);
                    }
                });
            }else{
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        currentLocationText.setText(getResources().getString(R.string.address_sheet_current_location));
                        Toast.makeText(getActivity(), "Please turn your GPS on", Toast.LENGTH_SHORT).show();
                    }
                }, 2000);
            }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

    private void getPermissionAndGetLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
                    }
                }, 5000);

            } else {
                getLocation();
            }
        } else {
            getLocation();
        }
    }
    public void extractAddressInfoAndShowInUI(List<Address> addresses){
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

        addressLine1.setText(add1);
        addressLine2.setText(add2);
        currentLocationText.setText(header);
        //For using in next fragment
        if(postalCode!=null) {
            pincode.setText(postalCode);
        }
        ArrayList<String> states=new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.india_states)));
        for(String s:states){
            if(s.toLowerCase().equals(adminArea.toLowerCase())){
                state.setText(s);
                break;
            }
        }
        ArrayList<String> cities=new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.indian_cities)));
        for(String c:cities){
            if(c.toLowerCase().equals(locality.toLowerCase())){
                city.setText(c);
                break;
            }
        }
    }
    public void activateListeners(){
        addressLine1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().length()>0){
                    isAddressOk=true;
                }else{
                    isAddressOk=false;
                }
                updateButtonUI();
            }
        });
        pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().length()==6){
                    isPincodeOK=true;
                }else{
                    isPincodeOK=false;
                }
                updateButtonUI();
            }
        });
        city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().length()>0){
                    isCityOk=true;
                }else{
                    isCityOk=false;
                }
                updateButtonUI();
            }
        });
        state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().length()>0){
                    isStateOk=true;
                }else{
                    isStateOk=false;
                }
                updateButtonUI();
            }
        });
    }
    public void updateButtonUI(){
        if(isAddressOk && isPincodeOK && isCityOk && isStateOk){
            confirmLocation.setAlpha(1f);
            confirmLocation.setEnabled(true);
        }else{
            confirmLocation.setAlpha(0.3f);
            confirmLocation.setEnabled(false);
        }
    }
}
