package com.example.farmerapp.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.farmerapp.API.ProductApi;
import com.example.farmerapp.Activities.SelectCropActivity;
import com.example.farmerapp.Retrofit.Crop;
import com.example.farmerapp.RetrofitClient.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectCropRepository {
    Application application;
    MutableLiveData<List<Crop>> crops;
    public SelectCropRepository(Application application) {
        this.application=application;
        crops=new MutableLiveData<List<Crop>>();
    }
    public LiveData<List<Crop>> getCrops(){
        Call<List<Crop>> call= RetrofitClient.getInstance(application).create(ProductApi.class).getCrops();
        call.enqueue(new Callback<List<Crop>>() {
            @Override
            public void onResponse(Call<List<Crop>> call, Response<List<Crop>> response) {
                if(response.isSuccessful()){
                    crops.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Crop>> call, Throwable t) {
               getCrops();
            }
        });
        return crops;
    }
}
