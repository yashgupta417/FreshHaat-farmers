package com.example.farmerapp.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.farmerapp.Repositories.LoginRepository;

public class LoginViewModel extends AndroidViewModel {
    LoginRepository loginRepository;
    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginRepository=new LoginRepository(application);
    }
    public LiveData<Integer> generateOTP(String mobileNumber){
        return loginRepository.generateOTP(mobileNumber);
    }


}
