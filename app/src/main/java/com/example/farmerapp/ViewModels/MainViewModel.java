package com.example.farmerapp.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.farmerapp.Data.Farmer;
import com.example.farmerapp.Repositories.MainRepository;

public class MainViewModel extends AndroidViewModel {
    MainRepository repository;
    public MainViewModel(@NonNull Application application) {
        super(application);
        repository=new MainRepository(application);
    }
    public LiveData<Farmer> getFarmer(){
        return repository.getFarmer();
    }
}
