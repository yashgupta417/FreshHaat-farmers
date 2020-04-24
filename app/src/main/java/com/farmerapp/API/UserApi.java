package com.farmerapp.API;

import com.farmerapp.Data.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApi {
    @POST("api/user/otp")
    Call<ResponseBody> generateOTP(@Body User user);

    @POST("api/user/otp/verify")
    Call<User> verifyOTP(@Body User request);

    @GET("api/user/getUser")
    Call<User> getUser();

    @GET("api/user/logout")
    Call<ResponseBody> logout();

}
