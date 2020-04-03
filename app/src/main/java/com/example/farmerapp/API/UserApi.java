package com.example.farmerapp.API;

import com.example.farmerapp.Retrofit.User;
import com.example.farmerapp.Retrofit.Verification;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApi {
    @POST("api/user/otp")
    Call<Verification> generateOTP(String mobileNumber);

    @POST("api/user/otp/verify")
    Call<Verification> verifyOTP(String mobileNumber,String OTP,String status);

    @GET("api/user/getUser")
    Call<User> getUserDetails();

}
