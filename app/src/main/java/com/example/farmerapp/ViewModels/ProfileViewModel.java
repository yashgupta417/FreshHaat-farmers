package com.example.farmerapp.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.farmerapp.Data.Farmer;
import com.example.farmerapp.Repositories.ProfileRepository;

public class ProfileViewModel extends AndroidViewModel{
    ProfileRepository repository;
    public ProfileViewModel(@NonNull Application application) {
        super(application);
        this.repository=new ProfileRepository(application);
    }
    public LiveData<Farmer> getFarmer(){
        return repository.getFarmer();
    }
}
