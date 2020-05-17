package com.farmerapp.API;

import com.farmerapp.Data.Banner;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BannerApi {
    @GET("api/banners/farmer")
    Call<List<Banner>> getBanners();
}
