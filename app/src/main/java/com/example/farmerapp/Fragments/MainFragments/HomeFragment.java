package com.example.farmerapp.Fragments.MainFragments;

import android.location.Address;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.farmerapp.Activities.MainActivity;
import com.example.farmerapp.Adapters.SellCropAdapter;
import com.example.farmerapp.Data.Farmer;
import com.example.farmerapp.R;
import com.example.farmerapp.Utils.LocationUtil;
import com.example.farmerapp.ViewModels.MainViewModel;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class HomeFragment extends Fragment {
    public HomeFragment() {

    }
    RecyclerView recyclerView;
    SellCropAdapter adapter;
    MainViewModel viewModel;
    GifImageView load;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView=v.findViewById(R.id.suggested_crops_recylerview);
        load=v.findViewById(R.id.load);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setHasFixedSize(true);
        viewModel= ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFarmer().observe(this, new Observer<Farmer>() {
            @Override
            public void onChanged(Farmer farmer) {
                load.setVisibility(View.GONE);
                adapter=new SellCropAdapter(farmer.getCrops(),getContext());
                recyclerView.setAdapter(adapter);
            }
        });
        getLocation();
        return v;
    }
    public void getLocation(){
        LocationUtil locationUtil=new LocationUtil(getActivity().getApplication());
        if(locationUtil.isLocationEnabled(getActivity())){
            locationUtil.observeAddress().observe(getActivity(), new Observer<List<Address>>() {
                @Override
                public void onChanged(List<Address> addresses) {
                    String locality = addresses.get(0).getLocality();
                    String subLocality = addresses.get(0).getSubLocality();
                    MainActivity.showLocation(locality,subLocality);
                }
            });
        }
    }
}
