package com.example.farmerapp.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.farmerapp.API.ProductApi;
import com.example.farmerapp.Data.Crop;
import com.example.farmerapp.RetrofitClient.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellRepository {
    Application application;
    public SellRepository(Application application) {
        this.application = application;
        fruits=new MutableLiveData<List<Crop>>();
        vegetables=new MutableLiveData<List<Crop>>();
    }
    MutableLiveData<List<Crop>> fruits,vegetables;
    public LiveData<List<Crop>> getFruits(){
        Call<List<Crop>> call= RetrofitClient.getInstance(application).create(ProductApi.class).getFruits();
        call.enqueue(new Callback<List<Crop>>() {
            @Override
            public void onResponse(Call<List<Crop>> call, Response<List<Crop>> response) {
                if(response.isSuccessful()){
                    fruits.setValue(response.body());
                    return;
                }
                getFruits();
            }

            @Override
            public void onFailure(Call<List<Crop>> call, Throwable t) {
                getFruits();
            }
        });
        return fruits;
    }
    public LiveData<List<Crop>> getVegetables(){
        Call<List<Crop>> call= RetrofitClient.getInstance(application).create(ProductApi.class).getVegetables();
        call.enqueue(new Callback<List<Crop>>() {
            @Override
            public void onResponse(Call<List<Crop>> call, Response<List<Crop>> response) {
                if(response.isSuccessful()){
                    vegetables.setValue(response.body());
                    return;
                }
                getVegetables();
            }

            @Override
            public void onFailure(Call<List<Crop>> call, Throwable t) {
                getVegetables();
            }
        });
        return vegetables;
    }
    Call<List<Crop>> call;
    public void queryVegetables(String query){
        call=RetrofitClient.getInstance(application).create(ProductApi.class).queryVegetables(query);
        call.enqueue(new Callback<List<Crop>>() {
            @Override
            public void onResponse(Call<List<Crop>> call, Response<List<Crop>> response) {
                if(response.isSuccessful()){
                    vegetables.setValue(response.body());
                    return;
                }
                call.clone().enqueue(this);
            }
            @Override
            public void onFailure(Call<List<Crop>> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });
    }
    public void queryFruits(String query){
        call=RetrofitClient.getInstance(application).create(ProductApi.class).queryFruits(query);
        call.enqueue(new Callback<List<Crop>>() {
            @Override
            public void onResponse(Call<List<Crop>> call, Response<List<Crop>> response) {
                if(response.isSuccessful()){
                    fruits.setValue(response.body());
                    return;
                }
                call.clone().enqueue(this);
            }
            @Override
            public void onFailure(Call<List<Crop>> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });
    }
}
