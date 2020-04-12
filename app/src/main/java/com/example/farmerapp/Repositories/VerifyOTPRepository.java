package com.example.farmerapp.Repositories;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.farmerapp.API.FarmerApi;
import com.example.farmerapp.API.UserApi;
import com.example.farmerapp.Activities.SplashActivity;
import com.example.farmerapp.Data.Farmer;
import com.example.farmerapp.Data.User;
import com.example.farmerapp.RetrofitClient.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.farmerapp.Activities.SplashActivity.ADDRESS;
import static com.example.farmerapp.Activities.SplashActivity.IS_LOGGED_IN;
import static com.example.farmerapp.Activities.SplashActivity.MOBILE_NO;
import static com.example.farmerapp.Activities.SplashActivity.TOKEN;
import static com.example.farmerapp.Activities.SplashActivity.USER_ID;

public class VerifyOTPRepository {
    Application application;
    SharedPreferences preferences;
    public static int  NEW_USER=1;
    public static int  OLD_USER=2;
    public VerifyOTPRepository(Application application) {
        this.application=application;
        preferences=application.getSharedPreferences(application.getPackageName(), Context.MODE_PRIVATE);
    }
    MutableLiveData<Integer> verify;
    public LiveData<Integer> verifyOTP(String mobileNumber, String otp){
        verify=new MutableLiveData<Integer>();
        User request=new User(mobileNumber);request.setStatus("farmer");
        request.setOtp(otp);
        Call<User> call=RetrofitClient.getInstance(application).create(UserApi.class).verifyOTP(request);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful() && response.body().getVerified()){
                    preferences.edit().putBoolean(IS_LOGGED_IN,true).apply();
                    User user=response.body();
                    preferences.edit().putString(TOKEN,user.getToken()).apply();
                    preferences.edit().putString(MOBILE_NO,user.getMob());
                    if(user.getNew_user()){
                        verify.setValue(NEW_USER);//When Farmer Details are not submitted
                    }else{
                        getFarmer();
                    }
                    return;
                }else{
                    verify.setValue(-1);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                verify.setValue(-1);
            }
        });
        return verify;
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
    public void getFarmer(){
        Call<Farmer> call=RetrofitClient.getInstance(application).create(FarmerApi.class).getFarmer();
        call.enqueue(new Callback<Farmer>() {
            @Override
            public void onResponse(Call<Farmer> call, Response<Farmer> response) {
                if(response.isSuccessful()){
                    Farmer farmer=response.body();
                    preferences.edit().putString(USER_ID,farmer.getId()).apply();
                    preferences.edit().putString(ADDRESS,farmer.getAddress());
                    preferences.edit().putBoolean(SplashActivity.IS_REGISTRATION_DONE,true).apply();
                    verify.setValue(OLD_USER);
                    return;
                }
            }

            @Override
            public void onFailure(Call<Farmer> call, Throwable t) {

            }
        });
    }
}
