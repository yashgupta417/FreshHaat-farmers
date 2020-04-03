package com.example.farmerapp.Repositories;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.farmerapp.Retrofit.Verification;
import com.example.farmerapp.RetrofitClient.OTPClient;
import com.example.farmerapp.RetrofitClient.UserClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {
    Application application;
    MutableLiveData<Integer> verification;
    public LoginRepository(Application application) {
        this.application=application;
        verification=new MutableLiveData<Integer>();
    }

    public LiveData<Integer> getOTPStatus(){
        return verification;
    }
    public void generateOTP(String mobileNumber){
        verification.setValue(0);
        Call<ResponseBody> call=new OTPClient().otpApi.generateOTP(new Verification(mobileNumber));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    verification.setValue(1);
                }else{
                    verification.setValue(-1);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                verification.setValue(-1);
            }
        });
    }

}
