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

public class SearchRepository{
    Application application;

    public SearchRepository(Application application) {
        this.application = application;
        result=new MutableLiveData<List<Crop>>();
    }
    Call<List<Crop>> call;
    public LiveData<List<Crop>> getResults(){
        return result;
    }
    MutableLiveData<List<Crop>> result;
    public void queryProducts(String query){
        call= RetrofitClient.getInstance(application).create(ProductApi.class).queryProducts(query);
        call.enqueue(new Callback<List<Crop>>() {
            @Override
            public void onResponse(Call<List<Crop>> call, Response<List<Crop>> response) {
                if(response.isSuccessful()){
                    result.setValue(response.body());
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
