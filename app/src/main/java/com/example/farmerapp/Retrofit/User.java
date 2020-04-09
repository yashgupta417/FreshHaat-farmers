package com.example.farmerapp.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("mob")
    @Expose
    private String mob;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("verified")
    @Expose
    private Boolean verified;
    @SerializedName("new_user")
    @Expose
    private Boolean new_user;



    public User(String mob) {
        this.mob = mob;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getMob() {
        return mob;
    }
    public void setMob(String mob) {
        this.mob = mob;
    }
    public Integer getV() {
        return v;
    }
    public void setV(Integer v) {
        this.v = v;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    public Boolean getVerified() {
        return verified;
    }
    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Boolean getNew_user() {
        return new_user;
    }

    public void setNew_user(Boolean new_user) {
        this.new_user = new_user;
    }
}