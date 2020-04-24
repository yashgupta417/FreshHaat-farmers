package com.farmerapp.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.farmerapp.Activities.MainActivity;
import com.farmerapp.Data.Crop;
import com.farmerapp.Repositories.SellRepository;

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
    public void queryProducts(String type,String query){
        if(type.equals(MainActivity.FRUITS))
            repository.queryFruits(query);
        else if(type.equals(MainActivity.VEGETABLES))
            repository.queryVegetables(query);
        return ;
    }

}
