package com.example.myapplication.database;

import android.annotation.SuppressLint;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class PostData {
    private String title;
    private String description;
    private String userId;
    private int price;
    private int starCount = 0;
    private String creation_date;
    private List<String> images;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public PostData(){

    }
    public PostData(String title, String description, int price, String userId,List<String> images){
        this.title = title;
        this.description = description;
        this.price = price;
        this.userId = userId;
        this.images = images;
        creation_date = DATE_FORMAT.format(new Date());
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", userId);
        result.put("title", title);
        result.put("price",price);
        result.put("description", description);
        result.put("starCount",starCount);
        result.put("images",images);
        result.put("creation_date",creation_date);
        return result;
    }

}
