package com.example.myapplication.database;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class PostData {
    public String title;
    public String description;
    public int price;
    public String userId;
    public int starCount = 0;
    public List<String> images;
    public PostData(){

    }
    public PostData(String title, String description, int price, String userId,List<String> images){
        this.title = title;
        this.description = description;
        this.price = price;
        this.userId = userId;
        this.images = images;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", userId);
        result.put("title", title);
        result.put("description", description);
        result.put("starCount",starCount);
        result.put("images",images);
        return result;
    }
}
