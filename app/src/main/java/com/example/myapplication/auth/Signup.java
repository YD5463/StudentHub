package com.example.myapplication.auth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Home;
import com.example.myapplication.R;
import com.example.myapplication.database.UserData;
import com.example.myapplication.utils.Utils;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.io.IOException;
import java.util.List;


public class Signup extends AppCompatActivity implements Validator.ValidationListener  {
    static private final String TAG = "Signup";

    private FirebaseAuth mAuth;

    private ImageView profile_image;
    private boolean is_profile_image_default = true;
    @NotEmpty() @Email()
    private EditText email;
    @Password(min=6)
    private EditText password;
    @ConfirmPassword
    private EditText confirm_password;
    @NotEmpty()
    private EditText fullname;
    @Pattern(regex = "0[0-9]{9}", message = "Please enter valid Phone number")
    private EditText phone_number;
    private Button signup_button;

    private ActivityResultLauncher<Intent> someActivityResultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        Uri imageUri = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                            profile_image.setImageBitmap(bitmap);
                            is_profile_image_default = false;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        init_variables();
        init_db();
        Validator validator = new Validator(this);
        validator.setValidationListener(this);
        signup_button.setOnClickListener((v)->validator.validate());
        profile_image.setOnClickListener(this::onImagePick); //TODO: change default image look
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
    private void init_db(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("UserData");
    }
    private void init_variables(){
        email = findViewById(R.id.email_signup_input);
        password = findViewById(R.id.password_signup_input);
        confirm_password = findViewById(R.id.password_signup_input_again);
        fullname = findViewById(R.id.fullname_signup_input);
        signup_button = findViewById(R.id.signup_btn);
        profile_image = findViewById(R.id.profile_image_picker);
        phone_number = findViewById(R.id.phone_number_signup);
    }
    private ProgressDialog createProgressDialog(){
        ProgressDialog mDialog = new ProgressDialog(Signup.this);
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(true);
        mDialog.show();
        return mDialog;
    }
    private UserData create_user(String image_url){
        UserData.Gender genderVal = UserData.Gender.MALE; //TODO: input this as well
        return new UserData(fullname.getText().toString(), email.getText().toString(),genderVal,image_url,phone_number.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    private void edit_user(String image_url,ProgressDialog mDialog){
        UserData data = create_user(image_url);
        Task<?> updateUserTask = FirebaseDatabase.getInstance().getReference("UserData")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(data);
        updateUserTask.addOnCompleteListener(subtask -> {
            mDialog.cancel();
            if (subtask.isSuccessful()) {
                Toast.makeText(Signup.this, "Successful Registered", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Signup.this, Home.class);
                startActivity(intent);
            } else {
                Log.e(TAG, "Error with userUpdate or profileImage upload");
            }
        });
    }
    @Override
    public void onValidationSucceeded() {
        ProgressDialog mDialog = createProgressDialog();
        Task<?> createUserTask = mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString());
        createUserTask.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if(!is_profile_image_default){
                            edit_user("",mDialog);
//                            Task<UploadTask.TaskSnapshot> uploadImageTask = FirebaseUtils.uploadImage(profile_image,UUID.randomUUID().toString());
//                            uploadImageTask.addOnCompleteListener(t2->{
//                                if(t2.isSuccessful()) {
//                                    edit_user(t2.getResult().getUploadSessionUri().toString(),mDialog);
//                                }//TODO: handle failed to upload
//                            });
                        }else{
                            edit_user("",mDialog);
                        }
                    } else {
                        Toast.makeText(Signup.this, "Check Email id or Password", Toast.LENGTH_SHORT).show();
                        mDialog.cancel();
                    }
                });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        Utils.onValidationFailed(errors,this);
    }
}