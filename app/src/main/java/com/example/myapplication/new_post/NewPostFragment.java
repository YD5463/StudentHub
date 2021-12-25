package com.example.myapplication.new_post;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.myapplication.Home;
import com.example.myapplication.database.DatabaseHandler;
import com.example.myapplication.R;
import com.example.myapplication.utils.Utils;
import com.example.myapplication.database.PostData;
import com.example.myapplication.databinding.FragmentNewPostBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.List;


public class NewPostFragment extends Fragment implements Validator.ValidationListener {
    static final private String TAG = "NewPostFragment";
    static final private int MAX_IMAGES = 3;

    private FragmentNewPostBinding binding;

    private int imagesCount = 0;
    private List<ImageView> images;
    @NotEmpty()
    private EditText title;
    @NotEmpty()
    private EditText description;

    private EditText price;
    private LinearLayout linearLayout;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;

    @SuppressLint("UseCompatLoadingForDrawables")
    private Drawable getDefaultImage(){
        Context context = getContext();
        return context!=null ? context.getDrawable(R.drawable.ic_baseline_image_24) : null;
    }
    private ImageView createDefaultImage(){
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(getDefaultImage());
        int curr_size = images.size();
        imageView.setOnClickListener((v)->onImagePick(curr_size));
        int width = (int)getResources().getDimension(R.dimen.add_post_image_width);
        int height = (int)getResources().getDimension(R.dimen.add_post_image_height);
        int padding = (int)getResources().getDimension(R.dimen.add_post_image_padding);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,height);
        imageView.setLayoutParams(lp);
        imageView.setPadding(padding,0,padding,padding*2);
        linearLayout.addView(imageView);
        return imageView;
    }
    private void onImagePick(int index){
        if(imagesCount!=0 && index < imagesCount){
            Utils.createBinaryAlert(()->{
                linearLayout.removeView(images.get(index));
                images.remove(index);
                imagesCount--;
            },()->{},"Are you sure?",getContext());
        }else{
            someActivityResultLauncher.launch(Utils.createImageChooserIntent());
        }
    }

    private int getPrice(){
        String priceStr = price.getText().toString();
        return !priceStr.isEmpty() ? Integer.parseInt(price.getText().toString()) : 0;
    }
    private void askLocationPermission(Runnable onGranted){
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                registerForActivityResult(new ActivityResultContracts.RequestPermission(),(isGranted2)->{
                    if(isGranted2){
                        onGranted.run();
                    }
                }).launch(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
        }).launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }
    private void upload_post(ProgressDialog mDialog,Context context,View v,List<String> imagesUris){
        String user_id =  FirebaseAuth.getInstance().getCurrentUser().getUid();
        Utils.getCurrLocation(getContext(), this::askLocationPermission,(location)->{
            PostData post = new PostData(title.getText().toString(),description.getText().toString(),
                    getPrice(),user_id,imagesUris,location);
            DatabaseHandler.addPost(post,()->{
                mDialog.cancel();
                cleanForm();
                Utils.hideKeyboardFrom(context,v);
                Toast.makeText(context, "Post Added Successfully.",Toast.LENGTH_SHORT).show();
                startActivity( new Intent(getActivity(), Home.class));
            },()->{
                Toast.makeText(context, "Add Post Failed.",Toast.LENGTH_SHORT).show();
                mDialog.cancel();
            });
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewPostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init(root);
        return root;
    }

    private void add_image(ActivityResult result){
        Utils.image_picker_handler(result,getContext(),(bitmap -> {
            images.get(images.size()-1).setImageBitmap(bitmap);
            imagesCount++;
            if(images.size()<MAX_IMAGES){
                images.add(createDefaultImage());
            }
        }));
    }
    private void init(View root){
        title = root.findViewById(R.id.post_title);
        description = root.findViewById(R.id.post_description);
        price = root.findViewById(R.id.post_price);
        linearLayout = root.findViewById(R.id.new_post_images);
        Button addPostBtn = root.findViewById(R.id.add_post_btn);

        images = new ArrayList<>(MAX_IMAGES);
        images.add(createDefaultImage());
        Validator validator = new Validator(this);
        validator.setValidationListener(this);
        addPostBtn.setOnClickListener(v -> validator.validate());
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::add_image);
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

    @Override
    public void onValidationSucceeded() {
        Context context = getContext();
        ProgressDialog mDialog = Utils.createProgressDialog(getContext());
        DatabaseHandler.uploadPostImages(images.subList(0,imagesCount),()->{
            mDialog.cancel();
            Toast.makeText(context,"Failed to upload post",Toast.LENGTH_SHORT).show();
        },(imageUris)->{
            upload_post(mDialog,context,getView(),imageUris);
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        Utils.onValidationFailed(errors,getContext());
    }
}