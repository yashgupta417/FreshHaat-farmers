package com.example.farmerapp.ViewModels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.farmerapp.Activities.SplashActivity;
import com.example.farmerapp.Data.Order;
import com.example.farmerapp.Repositories.RequestDetailRepository;

public class RequestDetailViewModel extends AndroidViewModel{
    RequestDetailRepository repository;
    public RequestDetailViewModel(@NonNull Application application) {
        super(application);
        repository=new RequestDetailRepository(application);
    }
    public LiveData<Order> getSellRequest(String orderId){
        SharedPreferences preferences=getApplication().getSharedPreferences(getApplication().getPackageName(), Context.MODE_PRIVATE);
        String userId=preferences.getString(SplashActivity.USER_ID,null);
        return repository.getSellRequest(userId,orderId);
    }
}
