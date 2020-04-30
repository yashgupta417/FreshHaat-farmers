package com.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;

import com.example.farmerapp.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class CollectionCentreActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_centre);
        SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Geocoder geocoder=new Geocoder(this);
        try {
            List<Address> address=geocoder.getFromLocationName("Opp Pkt-E Dilshad Garden Delhi-110095",1);
            Double lat=address.get(0).getLatitude();
            Double lng=address.get(0).getLongitude();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),15));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title("Nearest CC"));
        }catch (Exception e) {

        }

    }
    public void onBackClick(View v){
        finish();
    }
}
