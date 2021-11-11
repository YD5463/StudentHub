package com.example.myapplication.database;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Product {
    public String title;
    public String description;
    public int price;
    public String userId;
    public int starCount = 0;
    public Product(){

    }
    public Product(String title,String description,int price,String userId){
        this.title = title;
        this.description = description;
        this.price = price;
        this.userId = userId;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", userId);
        result.put("title", title);
        result.put("description", description);
        result.put("starCount",starCount);

        return result;
    }
}
