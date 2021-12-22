package com.example.myapplication.database;

import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.utils.Utils;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;


public class FirebaseDatabaseHandler {
    private static final String TAG = "FirebaseDatabaseHandler";
    private static final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private static final String POSTS_TABLE = "posts";
    private static final String USERS_TABLE = "UserData";
    private static final String POSTS_IMAGES = "posts-images";
    private static final String IMAGE_EXTENSION = ".jpg";
    private static final String PROFILE_IMAGES = "profile-images";

    static private void callOnFailed(@Nullable Runnable onFailed){
        if(onFailed != null){
            onFailed.run();
        }
    }
    static public void getUserById(@NonNull String userId,@NonNull Consumer<UserData> onSuccess,@Nullable Runnable onFailed) {
        database.child(USERS_TABLE+"/" + userId).get().addOnCompleteListener((task) -> {
            if(task.isSuccessful()){
                UserData user = task.getResult().getValue(UserData.class);
                if(user != null){
                    onSuccess.accept(user);
                }
                else{
                    Log.e(TAG,"getUserById: cant parse successful request response");
                    callOnFailed(onFailed);
                }

            }else{
                Log.e(TAG,"getUserById: request user by id="+userId+"failed");
                callOnFailed(onFailed);
            }
        });
    }
    static public void getCurrUserData(@NonNull Consumer<UserData> onSuccess,@Nullable Runnable onFailed){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            throw new IllegalStateException("user is not logged in");
        }
        getUserById(user.getUid(),onSuccess,onFailed);
    }
    static public void login(String email,String password,@NonNull Runnable onSuccess,@NonNull Consumer<String> onFailed){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "login successfully");
                        onSuccess.run();
                    } else {
                        Log.d(TAG, "login failed", task.getException());
                        onFailed.accept(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }
    public static void signup(UserData userData){

    }

    public static void addPost(PostData postData,Runnable onSuccess,Runnable onFailed){
        String key = database.child(POSTS_TABLE).push().getKey();
        Task<?> addToDBTask = database.child("/"+POSTS_TABLE+"/" + key).updateChildren(postData.toMap());
        addToDBTask.addOnCompleteListener(task->{
            if(task.isSuccessful()){
                onSuccess.run();
            }else{
                onFailed.run();
            }
        });
    }
    public static void uploadImages(List<ImageView> images,final int imagesCount,Runnable onFailed,Consumer<List<String>> onFinishUpload,String location,String imageExtension){
        List<String> imagesUris = new ArrayList<>();
        for(int i=0;i<imagesCount;i++){
            final StorageReference storageReference = FirebaseStorage.getInstance().
                    getReference(location+"/" + UUID.randomUUID().toString() + imageExtension);
            storageReference.putBytes(Utils.before_upload(images.get(i))).
                    addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().
                            addOnCompleteListener(upload_task -> {
                                imagesUris.add(upload_task.getResult().toString());
                                Log.d(TAG,upload_task.getResult().toString());
                                if(imagesUris.size() == imagesCount){
                                    onFinishUpload.accept(imagesUris);
                                }
                            })).
                            addOnFailureListener(e -> {
                                Log.e(TAG, "Image Uploading was failed:\n" + e.getMessage());
                                onFailed.run();
                            });
        }
    }

    public static void uploadPostImages(List<ImageView> images,final int imagesCount,Runnable onFailed,Consumer<List<String>> onFinishUpload){
           uploadImages(images,imagesCount,onFailed,onFinishUpload,POSTS_IMAGES,IMAGE_EXTENSION);
    }
    public static void uploadProfileImage(ImageView image,Runnable onFailed,Consumer<String> onFinishUpload){
        List<ImageView> images = new ArrayList<>();
        images.add(image);
        uploadImages(images,1,onFailed,
                (imagesUris)->onFinishUpload.accept(imagesUris.get(0)),
                PROFILE_IMAGES,IMAGE_EXTENSION);
    }


}
