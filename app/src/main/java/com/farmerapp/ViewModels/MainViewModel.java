package com.farmerapp.ViewModels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.farmerapp.Data.Crop;
import com.farmerapp.Data.Farmer;
import com.farmerapp.Data.Order;
import com.farmerapp.Repositories.MainRepository;

import java.util.List;

import static com.farmerapp.Activities.SplashActivity.USER_ID;

public class MainViewModel extends AndroidViewModel {
    MainRepository repository;
    String userId;
    public MainViewModel(@NonNull Application application) {
        super(application);
        SharedPreferences preferences=getApplication().getSharedPreferences(getApplication().getPackageName(), Context.MODE_PRIVATE);
        userId=preferences.getString(USER_ID,null);
        repository=new MainRepository(application);
    }
    public LiveData<List<Crop>> getSuggestedCrops(){
        return  repository.getSuggestedCrops(userId);
    }

    public LiveData<List<Order>> getAllRequests(){

        return repository.getAllRequests(userId);
    }

    public LiveData<Farmer> getFarmer(){
        return repository.getFarmer();
    }
}
