package com.example.farmerapp.ViewModels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.farmerapp.Data.Farmer;
import com.example.farmerapp.Data.Order;
import com.example.farmerapp.Repositories.MainRepository;

import java.util.List;

import static com.example.farmerapp.Activities.SplashActivity.USER_ID;

public class MainViewModel extends AndroidViewModel {
    MainRepository repository;
    public MainViewModel(@NonNull Application application) {
        super(application);
        repository=new MainRepository(application);
    }
    public LiveData<Farmer> getFarmer(){
        return repository.getFarmer();
    }

    public LiveData<List<Order>> getAllRequests(){
        SharedPreferences preferences=getApplication().getSharedPreferences(getApplication().getPackageName(), Context.MODE_PRIVATE);
        String userId=preferences.getString(USER_ID,null);
        return repository.getAllRequests(userId);
    }
}
