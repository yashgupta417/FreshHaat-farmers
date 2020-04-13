package com.example.farmerapp.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Order{
    @SerializedName("_id")
    @Expose
    private String databaseId;
    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("orderType")
    @Expose
    private String orderType;//pickup or manual
    @SerializedName("slotNumber")
    @Expose
    private String slotNumber;
    @SerializedName("pickupDate")
    @Expose
    private Date pickupDate;
    @SerializedName("pickupAddress")
    @Expose
    private Address pickupAddress;
    @SerializedName("order")
    @Expose
    private ArrayList<Crop> products;
    @SerializedName("totalPrice")
    @Expose
    private Float totalPrice;
    @SerializedName("orderDate")
    @Expose
    private String orderDate;
    @SerializedName("orderStatus")
    @Expose
    private String orderStatus;
    @SerializedName("slotStatus")
    @Expose
    private String slotStatus;
    @SerializedName("paymentStatus")
    @Expose
    private String paymentStatus;

    public Order() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    public Date getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(Date pickupDate) {
        this.pickupDate = pickupDate;
    }

    public Address getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(Address pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public ArrayList<Crop> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Crop> products) {
        this.products = products;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getSlotStatus() {
        return slotStatus;
    }

    public void setSlotStatus(String slotStatus) {
        this.slotStatus = slotStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }
}
