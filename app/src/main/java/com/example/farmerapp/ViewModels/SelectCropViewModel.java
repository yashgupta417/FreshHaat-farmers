package com.example.farmerapp.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.farmerapp.Repositories.SelectCropRepository;
import com.example.farmerapp.Retrofit.Crop;

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
}
