package com.example.myapplication.database;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class PostData implements Serializable {
    private String title;
    private String description;
    private String uid;
    private int price;
    private int starCount = 0;
    private String creation_date;
    private List<String> images;
    private GPSCoordinates seller_location;
//    private PostCatergory catergory;
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public String getUid() {
        return uid;
    }
    public int getPrice() {
        return price;
    }
    public int getStarCount() {
        return starCount;
    }
    public List<String> getImages() {
        return images;
    }
    public String getCreation_date() {
        return creation_date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setSeller_location(GPSCoordinates seller_location) {
        this.seller_location = seller_location;
    }

    public GPSCoordinates getSeller_location() {
        return seller_location;
    }
    public PostData(){}
    public PostData(String title, String description, int price, String userId, List<String> images, GPSCoordinates location){
        this.title = title;
        this.description = description;
        this.price = price;
        this.uid = userId;
        this.images = images;
        creation_date = DATE_FORMAT.format(new Date());
        this.seller_location = location;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("title", title);
        result.put("price",price);
        result.put("description", description);
        result.put("starCount",starCount);
        result.put("images",images);
        result.put("creation_date",creation_date);
        result.put("seller_location",seller_location);
        return result;
    }

    @NonNull
    public String toString(){
        return "Title: "+title+"\n"+"Description: "+description+"\n"+"Price: "+price+"\n"+"UserId: "
                + uid +"\n"+"Images: "+images+"\n"+"Creation date: "+creation_date + "\nlocation: "+seller_location;
    }
}
