package com.example.farmerapp.API;

import com.example.farmerapp.Retrofit.Farmer;
import com.example.farmerapp.Retrofit.Image;
import com.example.farmerapp.Retrofit.Verification;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FarmerApi {
    @POST("api/user/farmer")
    Call<Farmer> registerFarmerDetails(@Body Farmer farmer);

    @Multipart
    @POST("image/upload")
    Call<Image> uploadDP(@Part MultipartBody.Part image);
}
