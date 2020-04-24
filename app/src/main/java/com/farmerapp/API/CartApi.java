package com.farmerapp.API;

import com.farmerapp.Data.Cart;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CartApi {
    @POST("api/farmer/{id}/cart")
    Call<Cart> generateBill(@Body Cart cart, @Path("id") String userId);
}
