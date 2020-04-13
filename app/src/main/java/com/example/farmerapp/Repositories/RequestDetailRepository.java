package com.example.farmerapp.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.farmerapp.API.SellRequestApi;
import com.example.farmerapp.Data.Order;
import com.example.farmerapp.RetrofitClient.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestDetailRepository{
    Application application;
    public RequestDetailRepository(Application application){
        this.application = application;
        sellRequest=new MutableLiveData<Order>();
    }
    MutableLiveData<Order> sellRequest;
    public LiveData<Order> getSellRequest(String userId,String orderId){
        Call<Order> call= RetrofitClient.getInstance(application).create(SellRequestApi.class).getSellRequest(userId,orderId);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if(response.isSuccessful()){
                    sellRequest.setValue(response.body());
                    return;
                }
                call.clone();
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                call.clone();
            }
        });
        return sellRequest;
    }
}
