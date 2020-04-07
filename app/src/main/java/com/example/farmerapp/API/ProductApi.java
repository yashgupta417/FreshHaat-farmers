package com.example.farmerapp.API;

import com.example.farmerapp.Retrofit.Crop;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductApi {
    @GET("api/farmerproducts")
    Call<List<Crop>> getCrops();
}
