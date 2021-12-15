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
    private List<String> images = new ArrayList<>();
//    private PostCatergory catergory;
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public PostData clone(){
        return new PostData(title,description,price, uid,new ArrayList<>(images));
    }
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getCreation_date() {
        return creation_date;
    }

    public PostData(){

    }
    public PostData(String title, String description, int price, String userId,List<String> images){
        this.title = title;
        this.description = description;
        this.price = price;
        this.uid = userId;
        this.images = images;
        creation_date = DATE_FORMAT.format(new Date());
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
        return result;
    }

    @NonNull
    public String toString(){
        return "Title: "+title+"\n"+"Description: "+description+"\n"+"Price: "+price+"\n"+"UserId: "+ uid +"\n"+"Images: "+images+"\n"+"Creation date: "+creation_date;
    }
}
