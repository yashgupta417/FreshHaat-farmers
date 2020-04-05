package com.example.farmerapp.ViewModels;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.farmerapp.Activities.RegisterDetailsActivity;
import com.example.farmerapp.Repositories.RegisterDetailsRepository;
import com.example.farmerapp.Retrofit.Farmer;


public class RegisterDetailsViewModel extends AndroidViewModel {
    RegisterDetailsRepository repository;
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

