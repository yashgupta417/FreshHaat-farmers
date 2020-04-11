package com.example.farmerapp.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.farmerapp.Activities.MainActivity;
import com.example.farmerapp.Data.Crop;
import com.example.farmerapp.Repositories.SellRepository;

import java.util.List;

public class SellViewModel extends AndroidViewModel {
    SellRepository repository;
    public SellViewModel(@NonNull Application application) {
        super(application);
        repository=new SellRepository(application);
    }
    public LiveData<List<Crop>> getProducts(String type){
        if(type.equals(MainActivity.FRUITS))
            return repository.getFruits();
        else if(type.equals(MainActivity.VEGETABLES))
            return repository.getVegetables();
        return null;
    }

}