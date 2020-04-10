package com.example.farmerapp.API;

import com.example.farmerapp.Data.Farmer;
import com.example.farmerapp.Data.Image;

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

    @GET("api/user/farmer")
    Call<Farmer> getFarmer();

    @Multipart
    @POST("image/upload")
    Call<Image> uploadDP(@Part MultipartBody.Part image);
}
