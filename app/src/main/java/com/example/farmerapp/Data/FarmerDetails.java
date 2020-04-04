package com.example.farmerapp.Data;

import android.graphics.Bitmap;

public class FarmerDetails {

    Bitmap Profilephoto;
    String name,mob,address1,address2,place,landmark,pin,city,state;

    public FarmerDetails(Bitmap profilephoto, String name, String mob, String address1, String address2, String place, String landmark, String pin, String city, String state) {
        Profilephoto = profilephoto;
        this.name = name;
        this.mob = mob;
        this.address1 = address1;
        this.address2 = address2;
        this.place = place;
        this.landmark = landmark;
        this.pin = pin;
        this.city = city;
        this.state = state;
    }

    public FarmerDetails(String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public Bitmap getProfilephoto() {
        return Profilephoto;
    }

    public void setProfilephoto(Bitmap profilephoto) {
        Profilephoto = profilephoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public FarmerDetails() {
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
