package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;


public class Signup extends FormValidator {
    static private final String TAG = "Signup";
    private FirebaseAuth mAuth;

    @NotEmpty() @Email()
    private EditText email;
    @Password(min=6)
    private EditText password;
    @NotEmpty()
    private EditText fullname;

    private Button signup_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
        signup_button.setOnClickListener(this::onSubmit);
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("UserData");
    }
    private void init(){
        email = findViewById(R.id.email_signup_input);
        password = findViewById(R.id.password_signup_input);
        fullname = findViewById(R.id.fullname_signup_input);
        signup_button = findViewById(R.id.signup_btn);
    }

    @Override
    public void onValidationSucceeded() {
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
                        UserData data = new UserData(fullnameVal, emailVal, genderVal);
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