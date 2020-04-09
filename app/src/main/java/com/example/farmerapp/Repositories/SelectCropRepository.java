package com.example.farmerapp.Repositories;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.farmerapp.API.FarmerApi;
import com.example.farmerapp.API.ProductApi;
import com.example.farmerapp.Activities.SelectCropActivity;
import com.example.farmerapp.Retrofit.Crop;
import com.example.farmerapp.Retrofit.Farmer;
import com.example.farmerapp.RetrofitClient.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectCropRepository {
    Application application;
    MutableLiveData<List<Crop>> crops;
    MutableLiveData<Integer> uploadStatus;
    public SelectCropRepository(Application application) {
        this.application=application;
        crops=new MutableLiveData<List<Crop>>();
        uploadStatus=new MutableLiveData<Integer>();
    }
    public LiveData<List<Crop>> getCrops(){
        Call<List<Crop>> call= RetrofitClient.getInstance(application).create(ProductApi.class).getCrops();
        call.enqueue(new Callback<List<Crop>>() {
            @Override
            public void onResponse(Call<List<Crop>> call, Response<List<Crop>> response) {
                if(response.isSuccessful()){
                    crops.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Crop>> call, Throwable t) {
               getCrops();
            }
        });
        return crops;
    }

    public LiveData<Integer> postSelectedCrops(Farmer farmer){
        uploadStatus.setValue(0);
        Call<Farmer> call=RetrofitClient.getInstance(application).create(FarmerApi.class).registerFarmerDetails(farmer);
        call.enqueue(new Callback<Farmer>() {
            @Override
            public void onResponse(Call<Farmer> call, Response<Farmer> response) {
                if(response.isSuccessful()){
                    //Toast.makeText(application, "W", Toast.LENGTH_SHORT).show();
                    uploadStatus.setValue(1);
                    return;
                }
                uploadStatus.setValue(-1);
            }

            @Override
            public void onFailure(Call<Farmer> call, Throwable t) {
                    uploadStatus.setValue(-1);
            }
        });
        return uploadStatus;
    }
}
