package com.example.myapplication.database;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.function.Consumer;

public interface IDatabaseHandler {
    void getUserById(@NonNull String userId, @NonNull Consumer<UserData> onSuccess, @Nullable Runnable onFailed);
    void getCurrUserData(@NonNull Consumer<UserData> onSuccess, @Nullable Runnable onFailed);
    void login(String email,String password,@NonNull Runnable onSuccess,@NonNull Consumer<String> onFailed);
    void addPost(PostData postData,Runnable onSuccess,Runnable onFailed);
    void uploadImages(List<ImageView> images, final int imagesCount, Runnable onFailed, Consumer<List<String>> onFinishUpload, String location, String imageExtension);
    void uploadPostImages(List<ImageView> images,final int imagesCount,Runnable onFailed,Consumer<List<String>> onFinishUpload);
    void uploadProfileImage(ImageView image,Runnable onFailed,Consumer<String> onFinishUpload);
}