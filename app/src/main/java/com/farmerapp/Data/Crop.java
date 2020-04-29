package com.farmerapp.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Crop {
    @SerializedName(value="productId",alternate = {"_id"})
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("nameHindi")
    @Expose
    private String nameHindi;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("unitPrice")
    @Expose
    private Float price;
    @SerializedName("discountedPrice")
    @Expose
    private Float offerPrice;
    @SerializedName("discount")
    @Expose
    private Float offer;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("unitQty")
    @Expose
    private String batchSize;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("minQty")
    @Expose
    private Integer minQuantity;
    @SerializedName("maxQty")
    @Expose
    private Integer maxQuantity;
    @SerializedName("incrementVal")
    @Expose
    private Integer incrementValue;
    @SerializedName("checked")
    @Expose
    private Boolean isAvailable;
    public Crop() {
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(Integer minQuantity) {
        this.minQuantity = minQuantity;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public Integer getIncrementValue() {
        return incrementValue;
    }

    public void setIncrementValue(Integer incrementValue) {
        this.incrementValue = incrementValue;
    }

    public Float getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(Float offerPrice) {
        this.offerPrice = offerPrice;
    }

    public Float getOffer() {
        return offer;
    }

    public void setOffer(Float offer) {
        this.offer = offer;
    }

    public String getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public String getNameHindi() {
        return nameHindi;
    }

    public void setNameHindi(String nameHindi) {
        this.nameHindi = nameHindi;
    }
}
