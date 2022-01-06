package com.example.myapplication.business_entities;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserData {

    public enum Gender{ MALE, FEMALE, DECLINE_TO_ANSWER }
    public String fullname, email, profile_image_url, phone_number, uid;
    public Gender gender;
    public boolean isAdmin;
    public int reports;

    public UserData(){}

    public UserData(String fullname, String email, Gender gender,String profile_image_url,String phone_number, String uid) {
        this.fullname = fullname;
        this.email = email;
        this.gender = gender;
        this.profile_image_url = profile_image_url;
        this.isAdmin = false;
        this.phone_number = phone_number;
        this.uid = uid;
        reports = 0;

    }

    public String getName() {
        return fullname!=null ? fullname : "";
    }

    public void setName(String fullName) {
        this.fullname = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Exclude
    public String getUserType() {
        return isAdmin? "Admin": "Regular User";
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getReports() {
        return reports;
    }

    public void incrementReports() {
        reports++;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        return result;
    }
}
