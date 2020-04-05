package com.example.farmerapp.ViewModels;

import android.app.Application;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.farmerapp.Repositories.VerifyOTPRepository;

import java.util.Locale;

public class VerifyOTPViewModel extends AndroidViewModel {

    VerifyOTPRepository repository;
    MutableLiveData<Long> timeLeft;
    public VerifyOTPViewModel(@NonNull  Application application) {
        super(application);
        repository=new VerifyOTPRepository(application);
        timeLeft=new MutableLiveData<Long>();
    }
    public void verifyOTP(String mobileNumber,String otp){
        repository.verifyOTP(mobileNumber,otp);
    }

    public LiveData<Integer> checkResult(){
        return repository.checkResult();
    }
    public LiveData<Long> getTimeLeft(){
        return timeLeft;
    }
    public void startTimer(){
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
    }
    public void regenerateOTP(String mobileNumber){
        repository.generateOTP(mobileNumber);
    }
    public LiveData<Integer> getOTPStatus(){
        return repository.getOTPStatus();
    }
    public void getUser(){
        repository.getUser();
    }
}
