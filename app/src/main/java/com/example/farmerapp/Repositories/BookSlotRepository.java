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

public class BookSlotRepository{
    Application application;
    Order order;
    public BookSlotRepository(Application application) {
        this.application = application;
        order=new Order();
    }

    public String getOrderDatabaseId(){
        return order.getDatabaseId();
    }
    public LiveData<Integer> placeSellRequest(Order order1,String userId){
        MutableLiveData<Integer> result=new MutableLiveData<Integer>();
        result.setValue(0);
        Call<Order> call= RetrofitClient.getInstance(application).create(SellRequestApi.class).placeSellRequest(order1,userId);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if(response.isSuccessful()){
                    order=response.body();
                    result.setValue(1);
                    return;
                }
                result.setValue(-1);
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                result.setValue(-1);
            }
        });
        return result;
    }
}
