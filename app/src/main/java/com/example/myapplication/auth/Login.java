package com.example.myapplication.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.CustomButton;
import com.example.myapplication.Home;
import com.example.myapplication.R;
import com.example.myapplication.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;


public class Login extends AppCompatActivity implements Validator.ValidationListener {
    static private final String TAG = "Login";
    private FirebaseAuth mAuth;

    @NotEmpty() @Email()
    private EditText email;
    @Password(min=6)
    private EditText password;

    private Button forgot_password_btn, register_instead_btn;
    private Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        Validator validator = new Validator(this);
        validator.setValidationListener(this);
        mAuth = FirebaseAuth.getInstance();
        login_button.setOnClickListener((v)->validator.validate());
        forgot_password_btn.setOnClickListener(view -> startActivity( new Intent(Login.this, ForgotPassword.class)));
        register_instead_btn.setOnClickListener(view -> startActivity( new Intent(Login.this, Signup.class)));

    }
    private void init(){
        email = findViewById(R.id.email_input);
        password = findViewById(R.id.password_input);
        login_button = findViewById(R.id.login_btn);
        forgot_password_btn = findViewById(R.id.forgot_password);
        register_instead_btn = findViewById(R.id.register_instead);

    }

    @Override
    public void onValidationSucceeded() {
        String emailVal = email.getText().toString();
        String passwordVal = password.getText().toString();
        ProgressDialog mDialog = new ProgressDialog(Login.this);
        mDialog.setMessage(Login.this.getString(R.string.please_wait));
        mDialog.setCancelable(true);
        mDialog.show();
        mAuth.signInWithEmailAndPassword(emailVal, passwordVal)
                .addOnCompleteListener(this, task -> {
                    mDialog.cancel();
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        startActivity( new Intent(Login.this, Home.class));
                    } else {
//                      String cause = Objects.requireNonNull(task.getException()).getMessage();
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(Login.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        Utils.onValidationFailed(errors,this);
    }
}