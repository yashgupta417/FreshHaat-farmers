package com.example.farmerapp.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Cart {
    @SerializedName("cart")
    @Expose
    private ArrayList<Crop> products;
    @SerializedName("totalPrice")
    @Expose
    private Float totalPrice;
    @SerializedName("userId")
    @Expose
    private String userId;

    public Cart() {
    }

    public ArrayList<Crop> getCrops() {
        return products;
    }

    public void setCrops(ArrayList<Crop> cart) {
        this.products = products;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}