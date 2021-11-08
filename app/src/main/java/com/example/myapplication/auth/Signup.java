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

import com.cloudinary.android.MediaManager;
import com.example.myapplication.FormValidator;
import com.example.myapplication.Home;
import com.example.myapplication.R;
import com.example.myapplication.database.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.io.IOException;


public class Signup extends FormValidator {
    static private final String TAG = "Signup";

    private FirebaseAuth mAuth;

    private ImageView profile_image;

    @NotEmpty() @Email()
    private EditText email;
    @Password(min=6)
    private EditText password;
    @NotEmpty()
    private EditText fullname;

    private String image_url;
    private Button signup_button;

    private ActivityResultLauncher<Intent> someActivityResultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        MediaManager.init(this);
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
                            image_url = MediaManager.get().upload(imageUri).dispatch();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        init_variables();
        init_db();
        signup_button.setOnClickListener(this::onSubmit);
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
        fullname = findViewById(R.id.fullname_signup_input);
        signup_button = findViewById(R.id.signup_btn);
        profile_image = findViewById(R.id.profile_image_picker);
    }

    @Override
    public void onValidationSucceeded() {
        Log.d(TAG,image_url);
        String emailVal = email.getText().toString();
        String passwordVal = password.getText().toString();
        String fullnameVal = fullname.getText().toString();
        UserData.Gender genderVal = UserData.Gender.MALE;
        ProgressDialog mDialog = new ProgressDialog(Signup.this);
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(true);
        mDialog.show();
        mAuth.createUserWithEmailAndPassword(emailVal, passwordVal).addOnCompleteListener
                (task -> {
                    mDialog.cancel();
                    if (task.isSuccessful()) {
                        UserData data = new UserData(fullnameVal, emailVal, genderVal,image_url);
                        FirebaseDatabase.getInstance().getReference("UserData")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(data).
                                addOnCompleteListener(task1 -> {
//                                  signUp_progress.setVisibility(View.GONE);
                                    Toast.makeText(Signup.this, "Successful Registered", Toast.LENGTH_SHORT).show();
//                                    SharedPreferences sharedPref = Signup.this.getPreferences(Context.MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = sharedPref.edit();
//                                    editor.putString(getString(R.string.user), newHighScore);
//                                    editor.apply();
                                    Intent intent = new Intent(Signup.this, Home.class);
                                    startActivity(intent);

                                });
                    } else {
//                      signUp_progress.setVisibility(View.GONE);
                        Toast.makeText(Signup.this, "Check Email id or Password", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}