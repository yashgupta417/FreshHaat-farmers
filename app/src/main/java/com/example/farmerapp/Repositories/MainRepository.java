package com.example.farmerapp.Repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.farmerapp.API.FarmerApi;
import com.example.farmerapp.API.SellRequestApi;
import com.example.farmerapp.API.UserApi;
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
                call.clone();
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                call.clone();
            }
        });
        return requests;
    }
}
