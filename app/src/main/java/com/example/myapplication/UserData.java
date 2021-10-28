package com.example.myapplication;

public class UserData {
    public enum Gender{
        MALE,
        FEMALE,
        DECLINE_TO_ANSWER
    }
    public String fullname,email;
    public Gender gender;
    public UserData(){
    }
    public UserData(String fullname, String email, Gender gender) {
        this.fullname = fullname;
        this.email = email;
        this.gender = gender;
    }
}
