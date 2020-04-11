package com.example.farmerapp.Repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.farmerapp.API.FarmerApi;
import com.example.farmerapp.API.UserApi;
import com.example.farmerapp.Data.Farmer;
import com.example.farmerapp.RetrofitClient.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRepository {
    Application application;
    public MainRepository(Application application) {
        this.application=application;
        farmer=new MutableLiveData<Farmer>();
    }
    MutableLiveData<Farmer> farmer;
    public LiveData<Farmer> getFarmer(){
        Call<Farmer> call=RetrofitClient.getInstance(application).create(FarmerApi.class).getFarmer();
        call.enqueue(new Callback<Farmer>() {
            @Override
            public void onResponse(Call<Farmer> call, Response<Farmer> response) {
                if(response.isSuccessful()){
                   farmer.setValue(response.body());
                   return;
                }
                getFarmer();
            }

            @Override
            public void onFailure(Call<Farmer> call, Throwable t) {
                getFarmer();
            }
        });
        return farmer;
    }
}
