package com.example.myapplication.database;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserData {
    public enum Gender{
        MALE,
        FEMALE,
        DECLINE_TO_ANSWER
    }
    public String fullname,email,profile_image_url;
    public Gender gender;
    public boolean isAdmin;
    public String phone_number;
    public UserData(){
    }

    public UserData(String fullname, String email, Gender gender,String profile_image_url,String phone_number) {
        this.fullname = fullname;
        this.email = email;
        this.gender = gender;
        this.profile_image_url = profile_image_url;
        this.isAdmin = false;
        this.phone_number = phone_number;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        return result;
    }
}
