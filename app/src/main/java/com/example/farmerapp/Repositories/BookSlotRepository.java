package com.example.farmerapp.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.farmerapp.API.FarmerApi;
import com.example.farmerapp.API.SellRequestApi;
import com.example.farmerapp.Activities.SplashActivity;
import com.example.farmerapp.Data.Farmer;
import com.example.farmerapp.Data.Order;
import com.example.farmerapp.RetrofitClient.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.farmerapp.Activities.SplashActivity.USER_ID;

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

    public LiveData<Farmer> getFarmer(){
        MutableLiveData<Farmer> result=new MutableLiveData<Farmer>();
        Call<Farmer> call=RetrofitClient.getInstance(application).create(FarmerApi.class).getFarmer();
        call.enqueue(new Callback<Farmer>() {
            @Override
            public void onResponse(Call<Farmer> call, Response<Farmer> response) {
                if(response.isSuccessful()){
                    result.setValue(response.body());
                    return;
                }
            }

            @Override
            public void onFailure(Call<Farmer> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });
        return result;
    }
}
