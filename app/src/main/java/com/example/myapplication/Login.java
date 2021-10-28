package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Max;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.mobsandgeeks.saripaar.annotation.Url;

import java.util.List;


public class Login extends AppCompatActivity implements Validator.ValidationListener {
    static private final String TAG = "Login";
    private FirebaseAuth mAuth;

    @NotEmpty()
    @Email()
    private EditText email;

    @Password(min=6)
    private EditText password;

    private Button login_button;
    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        login_button.setOnClickListener(this::onLogin);
        mAuth = FirebaseAuth.getInstance();
        validator = new Validator(this);
        validator.setValidationListener(this);
    }
    private void init(){
        email = findViewById(R.id.email_input);
        password = findViewById(R.id.password_input);
        login_button = findViewById(R.id.login_btn);
    }
    private void onLogin(View v){
        validator.validate();
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
//                        String cause = Objects.requireNonNull(task.getException()).getMessage();
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(Login.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}