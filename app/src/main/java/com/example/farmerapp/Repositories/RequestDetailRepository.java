package com.example.farmerapp.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.farmerapp.API.SellRequestApi;
import com.example.farmerapp.Data.Order;
import com.example.farmerapp.RetrofitClient.RetrofitClient;

import okhttp3.ResponseBody;
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
                call.clone().enqueue(this);
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });
        return sellRequest;
    }
    public LiveData<Integer> cancelRequest(String userId,String orderId){
        MutableLiveData<Integer> result=new MutableLiveData<Integer>();
        Call<ResponseBody> call=RetrofitClient.getInstance(application).create(SellRequestApi.class).cancelSellRequest(userId,orderId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    result.setValue(1);
                    return;
                }
                result.setValue(-1);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                result.setValue(-1);
            }
        });
        return  result;
    }
}
