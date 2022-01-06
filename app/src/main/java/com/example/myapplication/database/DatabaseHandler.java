package com.example.myapplication.database;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class DatabaseHandler {
    private final FirebaseDatabaseHandler firebaseDatabaseHandler;

    public DatabaseHandler(Context context){
        this.firebaseDatabaseHandler = new FirebaseDatabaseHandler(context);
    }

    public static void getUserById(@NonNull String userId, @NonNull Consumer<UserData> onSuccess, @Nullable Runnable onFailed){
        FirebaseDatabaseHandler.getUserById(userId,onSuccess,onFailed);
    }

    public static void getCurrUserData(@NonNull Consumer<UserData> onSuccess, @Nullable Runnable onFailed){
        FirebaseDatabaseHandler.getCurrUserData(onSuccess,onFailed);
    }

    public static void login(String email, String password, @NonNull Runnable onSuccess, @NonNull Consumer<String> onFailed){
        FirebaseDatabaseHandler.login(email,password,onSuccess,onFailed);
    }

    public static void uploadPostImages(List<ImageView> images, Runnable onFailed,
                                 Consumer<List<String>> onFinishUpload){
        FirebaseDatabaseHandler.uploadPostImages(images, onFailed, onFinishUpload);
    }

    public static void uploadProfileImage(ImageView image, Runnable onFailed, Consumer<String> onFinishUpload){
        FirebaseDatabaseHandler.uploadProfileImage(image,onFailed,onFinishUpload);
    }


    public void addPost(PostData postData, Runnable onSuccess, Runnable onFailed){
        firebaseDatabaseHandler.addPost(postData,onSuccess,onFailed);
    }
}