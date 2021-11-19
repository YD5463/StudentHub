package com.example.myapplication.new_post;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.FirebaseUtils;
import com.example.myapplication.R;
import com.example.myapplication.Utils;
import com.example.myapplication.database.PostData;
import com.example.myapplication.databinding.FragmentNewPostBinding;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NewPostFragment extends Fragment {
    static final private String TAG = "NewPostFragment";
    static final private int MAX_IMAGES = 3;

    private NewPostViewModel newPostViewModel;
    private FragmentNewPostBinding binding;

    private int imagesCount = 0;
    private List<ImageView> images;
    private List<String> imagesUris;
    private EditText title;
    private EditText description;
    private EditText price;
    private LinearLayout linearLayout;
    private Button addPostBtn;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;

    @SuppressLint("UseCompatLoadingForDrawables")
    private Drawable getDefaultImage(){
        Context context = getContext();
        return context!=null ? context.getDrawable(R.drawable.ic_baseline_image_24) : null;
    }
    private ImageView createDefaultImage(){
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(getDefaultImage());
        imageView.setOnClickListener(this::onImagePick);
        int width = (int)getResources().getDimension(R.dimen.add_post_image_width);
        int height = (int)getResources().getDimension(R.dimen.add_post_image_height);
        int padding = (int)getResources().getDimension(R.dimen.add_post_image_padding);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,height);
        imageView.setLayoutParams(lp);
        imageView.setPadding(padding,0,padding,padding*2);
        linearLayout.addView(imageView);
        return imageView;
    }

    private ProgressDialog createProgressDialog(){
        ProgressDialog mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(true);
        mDialog.show();
        return mDialog;
    }
    private List<Task<UploadTask.TaskSnapshot>> upload(){
        List<Task<UploadTask.TaskSnapshot>> tasks = new ArrayList<>(imagesCount);
        for(int i=0;i<imagesCount;i++){
            tasks.add(FirebaseUtils.uploadImage(images.get(i), UUID.randomUUID().toString()));
        }
        return tasks;
    }
    private void onImagePick(View v){
        //TODO: check if image set
        final String TYPE = "image/*";
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType(TYPE);
        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,TYPE);
        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
        someActivityResultLauncher.launch(chooserIntent);
    }

    private int getPrice(){
        String priceStr = price.getText().toString();
        return !priceStr.isEmpty() ? Integer.parseInt(price.getText().toString()) : 0;
    }
    private void onSubmit(View v){
        List<Task<UploadTask.TaskSnapshot>> db_tasks = upload();
        Context context = getContext();
        ProgressDialog mDialog = createProgressDialog();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child("products").push().getKey();
        Tasks.whenAllComplete(db_tasks).addOnCompleteListener(t-> {
            for (Task<UploadTask.TaskSnapshot> db_task : db_tasks) {
                if(db_task.isSuccessful()){
                    String path = db_task.getResult().getUploadSessionUri().getPath();
                    Log.e(TAG,path);
                    imagesUris.add(path);
                }else{
                    Log.e(TAG,db_task.getException().getMessage());
                }
            }
            PostData post = new PostData(title.getText().toString(),description.getText().toString(), getPrice(),key,imagesUris);
            Map<String, Object> postValues = post.toMap();
            Task<?> addToDBTask = mDatabase.child("/posts/" + key).updateChildren(postValues);

            addToDBTask.addOnCompleteListener(task->{
                mDialog.cancel();
                if(task.isSuccessful()){
                    cleanForm();
                    Utils.hideKeyboardFrom(context,v);
                    Toast.makeText(context, "Post Added Successfully.",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Add Post Failed.",
                            Toast.LENGTH_SHORT).show();
                }
                });
            });
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        newPostViewModel = new ViewModelProvider(this).get(NewPostViewModel.class);

        binding = FragmentNewPostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init(root);
        return root;
    }
    private void init(View root){
        title = root.findViewById(R.id.post_title);
        description = root.findViewById(R.id.post_description);
        price = root.findViewById(R.id.post_price);
        linearLayout = root.findViewById(R.id.new_post_images);
        addPostBtn = root.findViewById(R.id.add_post_btn);

        images = new ArrayList<>(MAX_IMAGES);
        imagesUris = new ArrayList<>(MAX_IMAGES);
        images.add(createDefaultImage());
        addPostBtn.setOnClickListener(this::onSubmit);
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        Uri imageUri = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                            images.get(images.size()-1).setImageBitmap(bitmap);
                            imagesCount++;
                            if(images.size()<MAX_IMAGES){
                                images.add(createDefaultImage());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    void cleanForm(){
        title.setText("");
        description.setText("");
        price.setText("");
        imagesCount = 0;
        for(ImageView imageView : images){
            imageView.setImageResource(0);
        }
        images.get(0).setImageDrawable(getDefaultImage());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}