package com.example.farmerapp.API;

import com.example.farmerapp.Retrofit.Verification;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApi {
    @POST("api/user/otp")
    Call<ResponseBody> generateOTP(@Body Verification mobileNumber);

    @POST("api/user/otp/verify")
    Call<Verification> verifyOTP(@Body Verification request);

    @GET("api/user/getUser")
    Call<Verification> getUser();

}
