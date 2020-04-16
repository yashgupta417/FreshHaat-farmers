package com.example.farmerapp.ViewModels;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.farmerapp.Repositories.RegisterDetailsRepository;
import com.example.farmerapp.Data.Farmer;


public class RegisterDetailsViewModel extends AndroidViewModel {
    RegisterDetailsRepository repository;
    public Integer cityIndex=-1,stateIndex=-1;
    public Uri image;
    public Farmer farmer;
    public RegisterDetailsViewModel(@NonNull Application application) {
        super(application);
        repository=new RegisterDetailsRepository(application);
        farmer=new Farmer();

    }
    public void registerFarmerDetails(){
        repository.registerFarmerDetails(image,farmer);
    }
    public LiveData<Integer> getUploadStatus(){
        return repository.getUploadStatus();
    }

}

