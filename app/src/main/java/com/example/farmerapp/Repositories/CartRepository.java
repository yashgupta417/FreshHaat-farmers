package com.example.farmerapp.Repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.farmerapp.API.CartApi;
import com.example.farmerapp.Data.Cart;
import com.example.farmerapp.RetrofitClient.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartRepository {
    Application application;
    public CartRepository(Application application){
        this.application=application;
        finalCart=new MutableLiveData<Cart>();
    }
    MutableLiveData<Cart> finalCart;
    public LiveData<Cart> getBill(){
        return finalCart;
    }
    Call<Cart> call;
    public void generateBill(Cart localCart,String userId){
        call= RetrofitClient.getInstance(application).create(CartApi.class).generateBill(localCart,userId);
        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if(response.isSuccessful()){
                    finalCart.setValue(response.body());
                    return;
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                generateBill(localCart,userId);
            }
        });
    }
}
