package com.example.farmerapp.Repositories;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.farmerapp.API.UserApi;
import com.example.farmerapp.Retrofit.User;
import com.example.farmerapp.RetrofitClient.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOTPRepository {
    Application application;
    SharedPreferences preferences;
    public VerifyOTPRepository(Application application) {
        this.application=application;
        preferences=application.getSharedPreferences(application.getPackageName(), Context.MODE_PRIVATE);
    }
    public LiveData<Integer> verifyOTP(String mobileNumber, String otp){
        MutableLiveData<Integer> result=new MutableLiveData<Integer>();
        User request=new User(mobileNumber);
        request.setOtp(otp);
        Call<User> call=RetrofitClient.getInstance(application).create(UserApi.class).verifyOTP(request);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful() && response.body().getVerified()){
                    result.setValue(1);
                    preferences.edit().putBoolean("is_logged_in",true).apply();
                    //getUser();
                    return;
                }else{
                    result.setValue(-1);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                result.setValue(-1);
            }
        });
        return result;
    }

    public LiveData<Integer> generateOTP(String mobileNumber){
        MutableLiveData<Integer> result=new MutableLiveData<Integer>();
        Call<ResponseBody> call=RetrofitClient.getInstance(application).create(UserApi.class).generateOTP(new User(mobileNumber));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    result.setValue(1);
                }else{
                    result.setValue(-1);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                result.setValue(-1);
            }
        });
        return result;
    }

    //Not useful, just to check if user is logged_in or not
    public void getUser(){
        Call<User> call= RetrofitClient.getInstance(application).create(UserApi.class).getUser();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    Log.i("*****","working");
                    return;
                }
                Log.i("******","error");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("******",t.getLocalizedMessage());
            }
        });
    }

}
