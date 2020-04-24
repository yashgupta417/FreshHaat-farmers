package com.farmerapp.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.farmerapp.Repositories.SelectCropRepository;
import com.farmerapp.Data.Crop;
import com.farmerapp.Data.Farmer;

import java.util.ArrayList;
import java.util.List;

public class SelectCropViewModel extends AndroidViewModel {
    SelectCropRepository repository;
    public SelectCropViewModel(@NonNull Application application) {
        super(application);
        repository=new SelectCropRepository(application);
    }
    public LiveData<List<Crop>> getCrops(){
        return repository.getCrops();
    }

    public LiveData<Integer> postSelectedCrops(ArrayList<Crop> crops){
        Farmer farmer=new Farmer();
        farmer.setCrops(crops);
        return repository.postSelectedCrops(farmer);
    }
}
