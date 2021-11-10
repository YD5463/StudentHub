package com.example.myapplication.auth;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.example.myapplication.FormValidator;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;


public class Login extends FormValidator {
    static private final String TAG = "Login";
    private FirebaseAuth mAuth;

    @NotEmpty() @Email()
    private EditText email;
    @Password(min=6)
    private EditText password;

    private Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        init();
        login_button.setOnClickListener(this::onSubmit);
        mAuth = FirebaseAuth.getInstance();
    }
    private void init(){
        email = findViewById(R.id.email_input);
        password = findViewById(R.id.password_input);
        login_button = findViewById(R.id.login_btn);
    }

    @Override
    public void onValidationSucceeded() {
        String emailVal = email.getText().toString();
        String passwordVal = password.getText().toString();
        ProgressDialog mDialog = new ProgressDialog(Login.this);
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(true);
        mDialog.show();
        mAuth.signInWithEmailAndPassword(emailVal, passwordVal)
                .addOnCompleteListener(this, task -> {
                    mDialog.cancel();
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                    } else {
//                      String cause = Objects.requireNonNull(task.getException()).getMessage();
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(Login.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}