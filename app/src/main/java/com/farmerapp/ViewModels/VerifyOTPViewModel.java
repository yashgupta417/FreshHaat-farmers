package com.farmerapp.ViewModels;

import android.app.Application;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.farmerapp.Repositories.VerifyOTPRepository;

public class VerifyOTPViewModel extends AndroidViewModel {

    VerifyOTPRepository repository;

    public VerifyOTPViewModel(@NonNull  Application application) {
        super(application);
        repository=new VerifyOTPRepository(application);
    }
    public LiveData<Integer> verifyOTP(String mobileNumber,String otp){
        return  repository.verifyOTP(mobileNumber,otp);
    }

    public LiveData<Long> startTimer(){
        MutableLiveData<Long> timeLeft=new MutableLiveData<Long>();
        long OTP_TIME_LIMIT=1000*120;
        CountDownTimer timer=new CountDownTimer(OTP_TIME_LIMIT,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft.setValue(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                timeLeft.setValue(0l);
            }
        }.start();

        return timeLeft;
    }
    public LiveData<Integer> regenerateOTP(String mobileNumber){
       return repository.generateOTP(mobileNumber);
    }

    public void getUser(){
        repository.getUser();
    }
}
