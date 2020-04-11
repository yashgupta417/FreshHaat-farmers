package com.example.farmerapp.RetrofitClient;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.farmerapp.API.FarmerApi;
import com.example.farmerapp.API.UserApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
                    .addInterceptor(new Interceptor() {
                        @NotNull
                        @Override
                        public Response intercept(@NotNull Chain chain) throws IOException {
                            SharedPreferences preferences=context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
                            String token=preferences.getString("token",null);
                            Request request;
                            if(token!=null) {
                                request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + token).build();
                            }else{
                                request=chain.request().newBuilder().build();
                            }
                            return chain.proceed(request);
                        }
                    })
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
        }
        return retrofit;
    }

}
