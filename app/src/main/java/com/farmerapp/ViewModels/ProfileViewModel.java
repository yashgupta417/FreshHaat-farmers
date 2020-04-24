package com.farmerapp.ViewModels;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.farmerapp.Data.Farmer;
import com.farmerapp.Repositories.ProfileRepository;

public class ProfileViewModel extends AndroidViewModel{
    ProfileRepository repository;
    public Uri image;
    public ProfileViewModel(@NonNull Application application) {
        super(application);
        this.repository=new ProfileRepository(application);
    }
    public LiveData<Farmer> getFarmer(){
        return repository.getFarmer();
    }
    public LiveData<Integer> postChanges(Farmer farmer){
        return repository.postChanges(image, farmer);
    }
}
