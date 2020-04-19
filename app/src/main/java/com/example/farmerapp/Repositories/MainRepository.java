package com.example.farmerapp.Repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.farmerapp.API.FarmerApi;
import com.example.farmerapp.API.ProductApi;
import com.example.farmerapp.API.SellRequestApi;
import com.example.farmerapp.API.UserApi;
import com.example.farmerapp.Data.Cart;
import com.example.farmerapp.Data.Crop;
import com.example.farmerapp.Data.Farmer;
import com.example.farmerapp.Data.Order;
import com.example.farmerapp.RetrofitClient.RetrofitClient;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRepository {
    Application application;
    public MainRepository(Application application) {
        this.application=application;
    }

    public LiveData<List<Crop>> getSuggestedCrops(String userId){
        MutableLiveData<List<Crop>> suggestedCrops=new MutableLiveData<List<Crop>>();
        Call<List<Crop>> call=RetrofitClient.getInstance(application).create(FarmerApi.class).getSuggestedCrops(userId);
        call.enqueue(new Callback<List<Crop>>() {
            @Override
            public void onResponse(Call<List<Crop>> call, Response<List<Crop>> response) {
                if(response.isSuccessful()){
                    suggestedCrops.setValue(response.body());
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<Crop>> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });
        return suggestedCrops;
    }
    public LiveData<List<Order>> getAllRequests(String userId){
        MutableLiveData<List<Order>> requests=new MutableLiveData<List<Order>>();
        Call<List<Order>> call=RetrofitClient.getInstance(application).create(SellRequestApi.class).getAllSellRequests(userId);
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if(response.isSuccessful()){
                    requests.setValue(response.body());
                    return;
                }
                call.clone().enqueue(this);
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });
        return requests;
    }
}
