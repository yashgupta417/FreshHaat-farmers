package com.example.farmerapp.Fragments;

import android.Manifest;
import android.content.Context;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.SyncStateContract;
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
import android.widget.Toast;

import com.example.farmerapp.Activities.RegisterDetailsActivity;
import com.example.farmerapp.R;

import java.util.List;
import java.util.Locale;

public class RegisterAddress1Fragment extends Fragment {

     LocationManager locationMangaer = null;
     LocationListener locationListener = null;
     Location loc;
     private static final long MIN_DISTANCE_FOR_UPDATE = 10;
     private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;

    EditText address1,address2,landmark;
    ImageView back;
    LinearLayout location;
    Button next;
    Boolean isAddress1Ok=false,isAddress2OK=false;

    public RegisterAddress1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_reg_address1, container, false);
        address1=v.findViewById(R.id.address_1);
        address2=v.findViewById(R.id.address_2);
        landmark=v.findViewById(R.id.landmark);
        location=v.findViewById(R.id.location_ll);
        back=v.findViewById(R.id.back);
        next=v.findViewById(R.id.next);
        locationMangaer = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        addTextListeners();
        addButtonClickListeners();
        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==10){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                getCurrentAddress();
            }
        }
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
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getCurrentAddress();
                // temporarily done for testing
                updateNextButtonStatus();
                // it will also send the data directly to view model and skip
                // going to address2 fragment
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

    public void getCurrentAddress() {
        // Get the location manager
        locationMangaer = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (locationMangaer != null) {

            try {

                if (Build.VERSION.SDK_INT >= 23 && getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                   requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},10);
                }
                locationMangaer.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_FOR_UPDATE,
                        MIN_DISTANCE_FOR_UPDATE, locationListener);
            } catch (Exception ex) {
                Log.i("msg", "fail to request location update, ignore", ex);
            }
            if (locationMangaer != null) {
                loc = locationMangaer
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            Geocoder gcd = new Geocoder(getContext(),
                    Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
                if (addresses.size() > 0) {
                    String add1 = addresses.get(0).getAddressLine(0);
                    String add2 = addresses.size()>1?addresses.get(0).getAddressLine(1):"";
                    String locality = addresses.get(0).getLocality();
                    String subLocality = addresses.get(0).getSubLocality();
                    String state = addresses.get(0).getAdminArea();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    if(add2==null)add2="";
                    if(subLocality==null)subLocality=locality;

                    address1.setText(add1);
                    address2.setText(add2);
                    landmark.setText("");

                    RegisterDetailsActivity.details.setAddress1(add1);
                    RegisterDetailsActivity.details.setAddress2(add2);
                    RegisterDetailsActivity.details.setPlace(subLocality);
                    RegisterDetailsActivity.details.setPin(postalCode);
                    RegisterDetailsActivity.details.setLandmark("");
                    RegisterDetailsActivity.details.setState(state);
                    RegisterDetailsActivity.details.setCity(locality);
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error Fetching Location, Please Make Sure Your GPS is Enabled", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
