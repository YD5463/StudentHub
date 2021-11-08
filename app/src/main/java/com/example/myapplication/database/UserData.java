package com.example.myapplication.database;

public class UserData {
    public enum Gender{
        MALE,
        FEMALE,
        DECLINE_TO_ANSWER
    }
    public String fullname,email,profile_image_url;
    public Gender gender;
    public UserData(){
    }

    public UserData(String fullname, String email, Gender gender,String profile_image_url) {
        this.fullname = fullname;
        this.email = email;
        this.gender = gender;
        this.profile_image_url = profile_image_url;
    }
}
