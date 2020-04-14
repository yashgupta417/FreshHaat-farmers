package com.example.farmerapp.Repositories;

import android.app.Application;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.farmerapp.API.FarmerApi;
import com.example.farmerapp.Data.Farmer;
import com.example.farmerapp.Data.Image;
import com.example.farmerapp.RetrofitClient.RetrofitClient;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {
    Application application;

    public ProfileRepository(Application application) {
        this.application = application;
    }

    public LiveData<Farmer> getFarmer(){
        MutableLiveData<Farmer> farmer=new MutableLiveData<Farmer>();
        Call<Farmer> call= RetrofitClient.getInstance(application).create(FarmerApi.class).getFarmer();
        call.enqueue(new Callback<Farmer>() {
            @Override
            public void onResponse(Call<Farmer> call, Response<Farmer> response) {
                if(response.isSuccessful()){
                    farmer.setValue(response.body());
                    return;
                }
                call.clone().enqueue(this);
            }

            @Override
            public void onFailure(Call<Farmer> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });
        return farmer;
    }

    //Update Details Work
    MutableLiveData<Integer> updateStatus;
    public LiveData<Integer> postChanges(Uri image, Farmer farmer){
        updateStatus=new MutableLiveData<Integer>();
        uploadStep1(image,farmer);
        return updateStatus;
    }
    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = application.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    private void uploadStep1(Uri image,Farmer farmer){
        if(image==null) {
            uploadStep2(farmer);
            return;
        }
        File file = new File(getRealPathFromURI(image));
        File compressimagefile = null;
        try {
            compressimagefile = new Compressor(application).compressToFile(file);
        } catch (IOException e) {
            compressimagefile = file;
            e.printStackTrace();
        }
        final RequestBody requestBody = RequestBody.create(compressimagefile, MediaType.parse("multipart/form-data"));
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

        Call<Image> call=RetrofitClient.getInstance(application).create(FarmerApi.class).uploadDP(imagePart);
        call.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(Call<Image> call, Response<Image> response) {
                if(response.isSuccessful()){
                    Image imageResponse=response.body();
                    farmer.setProfilePhoto(imageResponse.getImgurl());
                    uploadStep2(farmer);
                    return;
                }
                updateStatus.setValue(-1);
            }
            @Override
            public void onFailure(Call<Image> call, Throwable t) {
                updateStatus.setValue(-1);
            }
        });
    }
    public void uploadStep2(Farmer farmer){
        Call<Farmer> call=RetrofitClient.getInstance(application).create(FarmerApi.class).registerFarmerDetails(farmer);
        call.enqueue(new Callback<Farmer>() {
            @Override
            public void onResponse(Call<Farmer> call, Response<Farmer> response) {
                if(response.isSuccessful()){
                    updateStatus.setValue(1);
                    return;
                }
                updateStatus.setValue(-1);
            }

            @Override
            public void onFailure(Call<Farmer> call, Throwable t) {
                updateStatus.setValue(-1);
            }
        });
    }
}
