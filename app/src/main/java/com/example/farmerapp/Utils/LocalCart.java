package com.example.farmerapp.Utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.farmerapp.Data.Crop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LocalCart {
    private static String PRODUCT_IDS="productIds";
    private static String QUANTITIES="quantities";
    public static void update(Application application,String id,String quantity){
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
        if(Integer.parseInt(quantity)>0) {
            productIds.add(id);
            quantities.add(quantity);
        }
        try {
            preferences.edit().putString(PRODUCT_IDS,ObjectSerializer.serialize(productIds)).apply();
            preferences.edit().putString(QUANTITIES,ObjectSerializer.serialize(quantities)).apply();
            Log.i("******",Integer.toString(productIds.size())+" "+quantity+" "+id);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public static ArrayList<String> getProductIds(Application application)  {
        SharedPreferences preferences=application.getSharedPreferences(application.getPackageName(), Context.MODE_PRIVATE);
        ArrayList<String> productIds= null;
        try {
            productIds = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString(PRODUCT_IDS,
                    ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return productIds;
    }
    private static ArrayList<String> getQuantities_String(Application application){
        SharedPreferences preferences=application.getSharedPreferences(application.getPackageName(), Context.MODE_PRIVATE);
        ArrayList<String> quantities= null;
        try {
            quantities = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString(QUANTITIES,
                    ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return quantities;
    }

    public static ArrayList<Integer> getQuantities(Application application){
        ArrayList<String> quantities_String=getQuantities_String(application);
        ArrayList<Integer> quantities=new ArrayList<Integer>();
        for(int i=0;i<quantities_String.size();i++){
            quantities.add(Integer.parseInt(quantities_String.get(i)));
        }
        return quantities;
    }
    public static ArrayList<Crop> syncQuantities(ArrayList<Crop> products,Application application){
        ArrayList<String> productIds=getProductIds(application);
        ArrayList<Integer> quantities=getQuantities(application);
        HashMap<String,Integer> map = new HashMap<String, Integer>();
        for(int i=0;i<productIds.size();i++){
            map.put(productIds.get(i),quantities.get(i));
        }
        for(int i=0;i<products.size();i++){
            Integer q=map.get(products.get(i).getId());
            if(q!=null){
                products.get(i).setQuantity(q);
            }else{
                products.get(i).setQuantity(0);
            }
        }
        return products;
    }
}
