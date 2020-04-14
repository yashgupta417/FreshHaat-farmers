package com.example.farmerapp.API;

import com.example.farmerapp.Data.Crop;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProductApi {
    @GET("api/farmerproducts")
    Call<List<Crop>> getCrops();

    @GET("api/farmerproducts/vegetables")
    Call<List<Crop>> getVegetables();

    @GET("api/farmerproducts/fruits")
    Call<List<Crop>> getFruits();

    @GET("api/farmerproducts/search")
    Call<List<Crop>> queryProducts(@Query("value")String query);

    @GET("api/farmerproducts/vegetables/search")
    Call<List<Crop>> queryVegetables(@Query("value")String query);

    @GET("api/farmerproducts/fruits/search")
    Call<List<Crop>> queryFruits(@Query("value")String query);
}
