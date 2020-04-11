package com.example.farmerapp.Utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.farmerapp.Data.Crop;

import java.io.IOException;
import java.util.ArrayList;

public class LocalCart {
    private static String PRODUCT_IDS="productIds";
    private static String QUANTITIES="quantities";
    public static void update(Application application,String id,String quantity) throws IOException {
        SharedPreferences preferences=application.getSharedPreferences(application.getPackageName(), Context.MODE_PRIVATE);
        ArrayList<String> productIds=getProductIds(application);
        ArrayList<String> quantities=getQuantities_String(application);
        for(int i=0;i<productIds.size();i++){
            if(productIds.get(i).equals(id)){
                productIds.remove(i);
                quantities.remove(i);
                break;
            }
        }
        if(Integer.parseInt(quantity)>0){
            productIds.add(id);
            quantities.add(quantity);
            preferences.edit().putString(PRODUCT_IDS,ObjectSerializer.serialize(productIds)).apply();
            preferences.edit().putString(QUANTITIES,ObjectSerializer.serialize(quantities)).apply();
        }
    }
    public static ArrayList<String> getProductIds(Application application) throws IOException {
        SharedPreferences preferences=application.getSharedPreferences(application.getPackageName(), Context.MODE_PRIVATE);
        ArrayList<String> productIds=(ArrayList<String>) ObjectSerializer.deserialize(preferences.getString(PRODUCT_IDS,
                ObjectSerializer.serialize(new ArrayList<String>())));
        return productIds;
    }
    private static ArrayList<String> getQuantities_String(Application application) throws IOException {
        SharedPreferences preferences=application.getSharedPreferences(application.getPackageName(), Context.MODE_PRIVATE);
        ArrayList<String> quantities=(ArrayList<String>) ObjectSerializer.deserialize(preferences.getString(QUANTITIES,
                ObjectSerializer.serialize(new ArrayList<String>())));
        return quantities;
    }

    public static ArrayList<Integer> getQuantities(Application application) throws IOException {
        ArrayList<String> quantities_String=getQuantities_String(application);
        ArrayList<Integer> quantities=new ArrayList<Integer>();
        for(int i=0;i<quantities_String.size();i++){
            quantities.add(Integer.parseInt(quantities_String.get(i)));
        }
        return quantities;
    }
}