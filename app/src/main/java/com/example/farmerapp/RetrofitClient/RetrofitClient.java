package com.example.farmerapp.RetrofitClient;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.farmerapp.API.FarmerApi;
import com.example.farmerapp.API.UserApi;
import com.example.farmerapp.Activities.LoginActivity;
import com.example.farmerapp.Activities.SplashActivity;
import com.example.farmerapp.Utils.LocalCart;
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
    public static OkHttpClient okHttpClient;
    public static  Retrofit getInstance(Application application){
        if(retrofit==null){
            Gson gson= new GsonBuilder().serializeNulls().create();
            HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor();
            loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
            okHttpClient=new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @NotNull
                        @Override
                        public Response intercept(@NotNull Chain chain) throws IOException {
                            SharedPreferences preferences=application.getSharedPreferences(application.getPackageName(),Context.MODE_PRIVATE);
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
                    .addInterceptor(new Interceptor() {
                        @NotNull
                        @Override
                        public Response intercept(@NotNull Chain chain) throws IOException {
                            Request request=chain.request();
                            Response response=chain.proceed(request);
                            //Token Expiry Handling
                            if(response.code()==403){
                                LocalCart.emptyCart(application);
                                SharedPreferences preferences=application.getSharedPreferences(application.getPackageName(),Context.MODE_PRIVATE);
                                preferences.edit().putBoolean(SplashActivity.IS_LOGGED_IN,false).apply();
                                preferences.edit().putBoolean(SplashActivity.IS_REGISTRATION_DONE,false).apply();
                                preferences.edit().putString(SplashActivity.TOKEN,null).apply();
                                Intent intent=new Intent(application, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                application.startActivity(intent);
                            }
                            return  response;
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
