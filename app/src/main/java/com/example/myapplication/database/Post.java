package com.example.myapplication.database;

public class Post {
    private String title, description, uid;
    private int starCount, price;

    public Post() {
    }

    public Post(String title, String description, int starCount, int price, String uid) {
        this.title = title;
        this.description = description;
        this.starCount = starCount;
        this.price = price;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}