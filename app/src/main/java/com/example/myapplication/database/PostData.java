package com.example.myapplication.database;

public class PostData {
    private String title, description;
    private int starCount, price;

    public PostData(String title, String description, int starCount, int price) {
        this.title = title;
        this.description = description;
        this.starCount = starCount;
        this.price = price;
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
