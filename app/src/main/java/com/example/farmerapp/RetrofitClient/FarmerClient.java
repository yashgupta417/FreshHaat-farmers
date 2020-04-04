package com.example.farmerapp.RetrofitClient;

import com.example.farmerapp.API.FarmerApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FarmerClient {
    public Retrofit retrofit;
    public FarmerApi api;
    public FarmerClient() {
        Gson gson= new GsonBuilder().serializeNulls().create();
        HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor();
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();
        retrofit=new Retrofit.Builder()
                .baseUrl("http://ec2-18-217-77-247.us-east-2.compute.amazonaws.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        api=retrofit.create(FarmerApi.class);
    }

}
