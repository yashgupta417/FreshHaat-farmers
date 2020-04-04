package com.example.farmerapp.Repositories;

import android.app.Application;

import com.example.farmerapp.RetrofitClient.FarmerClient;

public class RegisterDetailsRepository {
    Application application;
    FarmerClient client;

    public RegisterDetailsRepository(Application application) {
        this.application = application;
        this.client = new FarmerClient();
    }
    public void uploadDP(){

    }
    public void registerAsFarmer(){

    }
}
