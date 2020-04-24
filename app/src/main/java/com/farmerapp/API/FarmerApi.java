package com.farmerapp.API;

import com.farmerapp.Data.Crop;
import com.farmerapp.Data.Farmer;
import com.farmerapp.Data.Image;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface FarmerApi {
    @POST("api/user/farmer")
    Call<Farmer> registerFarmerDetails(@Body Farmer farmer);

    @GET("api/user/farmer")
    Call<Farmer> getFarmer();

    @Multipart
    @POST("image/upload")
    Call<Image> uploadDP(@Part MultipartBody.Part image);

    @GET("api/farmer/{id}/suggestions")
    Call<List<Crop>> getSuggestedCrops(@Path("id") String userId);
}
