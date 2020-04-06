package com.example.farmerapp.RetrofitClient;

import android.app.Application;
import android.content.Context;

import com.example.farmerapp.API.FarmerApi;
import com.example.farmerapp.API.UserApi;
import com.example.farmerapp.Interceptors.AddCookiesInterceptor;
import com.example.farmerapp.Interceptors.ReceivedCookiesInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.CookieJar;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.net.CookiePolicy.ACCEPT_ALL;

public class RetrofitClient {
    public static Retrofit retrofit;

    public static  Retrofit getInstance(Context context){
        if(retrofit==null){
            Gson gson= new GsonBuilder().serializeNulls().create();
            HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor();
            loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient=new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(new ReceivedCookiesInterceptor(context))
                    .addInterceptor(new AddCookiesInterceptor(context))
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .writeTimeout(3, TimeUnit.MINUTES)
                    .readTimeout(2, TimeUnit.MINUTES)
                    .build();
            retrofit=new Retrofit.Builder()
                    .baseUrl("http://ec2-18-217-77-247.us-east-2.compute.amazonaws.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

}
