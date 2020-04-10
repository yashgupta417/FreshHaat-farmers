package com.example.farmerapp.Repositories;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
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

public class RegisterDetailsRepository {
    Application application;
    MutableLiveData<Integer> uploadStatus;
    SharedPreferences preferences;
    public RegisterDetailsRepository(Application application) {
        this.application = application;
        uploadStatus=new MutableLiveData<Integer>();
        preferences=application.getSharedPreferences(application.getPackageName(), Context.MODE_PRIVATE);
    }
    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = application.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    public LiveData<Integer> getUploadStatus(){
        return uploadStatus;
    }
    public void registerFarmerDetails(Uri image,Farmer farmer){
        uploadStatus.setValue(0);
        uploadStep1(image,farmer);
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
                    //Toast.makeText(application, "Image uploaded", Toast.LENGTH_SHORT).show();
                    uploadStep2(farmer);
                    return;
                }
                uploadStatus.setValue(-1);
            }
            @Override
            public void onFailure(Call<Image> call, Throwable t) {
                uploadStatus.setValue(-1);
            }
        });
    }
    private void uploadStep2(Farmer farmer){
        Call<Farmer> call=RetrofitClient.getInstance(application).create(FarmerApi.class).registerFarmerDetails(farmer);
        call.enqueue(new Callback<Farmer>() {
            @Override
            public void onResponse(Call<Farmer> call, Response<Farmer> response) {
                if(response.isSuccessful()){
                    //Toast.makeText(application, "Details uploaded", Toast.LENGTH_SHORT).show();
                    preferences.edit().putBoolean("is_registration_done",true).apply();
                    uploadStatus.setValue(1);
                }
                uploadStatus.setValue(-1);
            }

            @Override
            public void onFailure(Call<Farmer> call, Throwable t) {
                uploadStatus.setValue(-1);
            }
        });
    }
}
