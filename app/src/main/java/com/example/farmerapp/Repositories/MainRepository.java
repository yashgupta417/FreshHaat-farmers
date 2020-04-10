package com.example.farmerapp.Repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.farmerapp.API.UserApi;
import com.example.farmerapp.RetrofitClient.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRepository {
    Application application;
    public MainRepository(Application application) {
        this.application=application;

    }
    public LiveData<Integer> logout(){
        MutableLiveData<Integer> requestStatus=new MutableLiveData<Integer>();
        requestStatus.setValue(0);
        Call<ResponseBody> call= RetrofitClient.getInstance(application).create(UserApi.class).logout();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    requestStatus.setValue(1);
                    return;
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                requestStatus.setValue(-1);
                Log.i("*********88",t.getLocalizedMessage());
            }
        });
        return requestStatus;
    }
}
