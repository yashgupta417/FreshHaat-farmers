package com.example.farmerapp.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.farmerapp.API.FarmerApi;
import com.example.farmerapp.Data.Farmer;
import com.example.farmerapp.RetrofitClient.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {
    Application application;

    public ProfileRepository(Application application) {
        this.application = application;
    }

    public LiveData<Farmer> getFarmer(){
        MutableLiveData<Farmer> farmer=new MutableLiveData<Farmer>();
        Call<Farmer> call= RetrofitClient.getInstance(application).create(FarmerApi.class).getFarmer();
        call.enqueue(new Callback<Farmer>() {
            @Override
            public void onResponse(Call<Farmer> call, Response<Farmer> response) {
                if(response.isSuccessful()){
                    farmer.setValue(response.body());
                    return;
                }
                call.clone().enqueue(this);
            }

            @Override
            public void onFailure(Call<Farmer> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });
        return farmer;
    }
}
