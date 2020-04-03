package com.example.farmerapp.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.farmerapp.API.OTPApi;
import com.example.farmerapp.Retrofit.Verification;
import com.example.farmerapp.RetrofitClient.OTPClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOTPRepository {
    Application application;
    OTPClient otpClient;
    MutableLiveData<Integer> result;
    public VerifyOTPRepository(Application application) {
        this.application=application;
        otpClient=new OTPClient();
        result=new MutableLiveData<Integer>();
        verification=new MutableLiveData<Integer>();
    }
    public LiveData<Integer> checkResult(){
        return result;
    }
    public void verifyOTP(String mobileNumber,String otp){
        result.setValue(0);
        Verification request=new Verification(mobileNumber);
        request.setOtp(otp);
        Call<Verification> call=otpClient.otpApi.verifyOTP(request);
        call.enqueue(new Callback<Verification>() {
            @Override
            public void onResponse(Call<Verification> call, Response<Verification> response) {
                if(response.isSuccessful() && response.body().getVerified()){
                    result.setValue(1);
                    return;
                }else{
                    result.setValue(-1);
                }
            }

            @Override
            public void onFailure(Call<Verification> call, Throwable t) {
                result.setValue(-1);
            }
        });
    }
    MutableLiveData<Integer> verification;
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
