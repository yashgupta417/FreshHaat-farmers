package com.example.farmerapp.API;

import com.example.farmerapp.Data.Order;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SellRequestApi {
    @POST("api/farmer/{id}/order")
    Call<Order> placeSellRequest(@Body Order order, @Path("id") String userId);

    @PUT("api/farmer/{id}/order/{oid}/cancel")
    Call<ResponseBody> cancelSellRequest(@Path("id") String userId,@Path("oid") String orderId);

    @GET("api/farmer/{id}/order")
    Call<List<Order>> getAllSellRequests(@Path("id")String userId);

    @GET("api/farmer/{id}/order/{oid}")
    Call<Order> getSellRequest(@Path("id") String userId,@Path("oid") String orderId);
}
