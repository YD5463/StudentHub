package com.example.myapplication.ui.posts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.example.myapplication.R;
import com.example.myapplication.Utils;
import com.example.myapplication.database.PostData;
import com.example.myapplication.databinding.FragmentNewPostBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostsFragment extends Fragment {
    static final private int MAX_IMAGES = 3;

    private PostsViewModel postsViewModel;
    private FragmentNewPostBinding binding;
    private StorageReference storageRef;

    private List<ImageView> images;

    private EditText title;
    private EditText description;
    private EditText price;
    private LinearLayout linearLayout;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;


    private ImageView createDefaultImage(){
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(getContext().getDrawable(R.drawable.ic_baseline_image_24));
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


    private List<String> upload(){
        List<String> urls = new ArrayList<>(images.size());
        for(ImageView image : images){
            Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = storageRef.putBytes(data);
        }
        return urls;
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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        postsViewModel = new ViewModelProvider(this).get(PostsViewModel.class);

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

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        images = new ArrayList<>(MAX_IMAGES);
        images.add(createDefaultImage());
        Button btn = root.findViewById(R.id.add_post_btn);
        btn.setOnClickListener(this::onSubmit);
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
                            if(images.size()<MAX_IMAGES){
                                images.add(createDefaultImage());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    private int getPrice(){
        String priceStr = price.getText().toString();
        return !priceStr.isEmpty() ? Integer.parseInt(price.getText().toString()) : 0;
    }
    private void onSubmit(View v){
        Context context = getContext();
        ProgressDialog mDialog = new ProgressDialog(context);
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(true);
        mDialog.show();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child("products").push().getKey();
        PostData post = new PostData(title.getText().toString(),description.getText().toString(), getPrice(),key);
        Map<String, Object> postValues = post.toMap();
        mDatabase.child("/posts/" + key).updateChildren(postValues).
                addOnCompleteListener(task->{
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
    }
    void cleanForm(){
        title.setText("");
        description.setText("");
        price.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}