package com.example.daniel.ee461l_hw4;

/**
 * Created by daniel on 4/6/18.
 */

public class GameInfo {
    private double lat;
    private double lng;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
