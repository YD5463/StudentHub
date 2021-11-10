package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.cloudinary.android.MediaManager;

import java.io.IOException;

public class ProfileImage extends View {
    private ActivityResultLauncher<Intent> someActivityResultLauncher;
    private ImageView profile_image;
    private String image_url;

    public ProfileImage(Context context) {
        super(context);
    }

    public ProfileImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        MediaManager.init(context);

//        someActivityResultLauncher = context.registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        Intent data = result.getData();
//                        assert data != null;
//                        Uri imageUri = data.getData();
//                        try {
//                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
//                            profile_image.setImageBitmap(bitmap);
//                            image_url = MediaManager.get().upload(imageUri).dispatch();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
        profile_image.setOnClickListener(this::onImagePick);
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
    public ProfileImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


}