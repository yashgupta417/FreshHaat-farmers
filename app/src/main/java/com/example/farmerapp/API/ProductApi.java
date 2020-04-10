package com.example.farmerapp.API;

import com.example.farmerapp.Data.Crop;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductApi {
    @GET("api/farmerproducts")
    Call<List<Crop>> getCrops();

    @GET("api/farmerproducts/vegetables")
    Call<List<Crop>> getVegetables();

    @GET("api/farmerproducts/fruits")
    Call<List<Crop>> getFruits();
}
