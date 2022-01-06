package com.example.myapplication.business_entities;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class GPSCoordinates implements Serializable {
    private float longitude;
    private float latitude;

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public GPSCoordinates(){}
    public GPSCoordinates(double latitude, double longitude) {
        this.longitude = (float) longitude;
        this.latitude = (float) latitude;
    }
}
