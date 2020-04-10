package com.example.farmerapp.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.farmerapp.API.UserApi;
import com.example.farmerapp.Data.User;
import com.example.farmerapp.RetrofitClient.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {
    Application application;
    public LoginRepository(Application application) {
        this.application=application;

    }
    public LiveData<Integer> generateOTP(String mobileNumber){
        MutableLiveData<Integer> result=new MutableLiveData<Integer>();
        result.setValue(0);
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

}
