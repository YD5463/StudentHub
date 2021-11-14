package com.example.myapplication.auth;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.example.myapplication.R;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private EditText inputEmail;
    private Button reset_button;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        progressDialog = new ProgressDialog(this);
        inputEmail = (EditText) findViewById(R.id.forgotemail);
        reset_button = (Button) findViewById(R.id.submitButton);
        auth = FirebaseAuth.getInstance();

        // Listen for when the user clicks the "Forgot Password" button
        reset_button.setOnClickListener(v -> {

            String email = inputEmail.getText().toString().trim();

            // Making sure the input email text is not empty
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplication(), "Enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            // Output a message on the screen to indicate the reset password email is being sent
            progressDialog.setMessage(" Sending Email ...");
            progressDialog.show();
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        progressDialog.dismiss();
                        // if the mail is sent successfully, then the app outputs a message and redirects to the login page
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPassword.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                            startActivity( new Intent(ForgotPassword.this, Login.class));
                        }
                        // Otherwise we send a failure message and the user should try again.
                        else {
                            Toast.makeText(ForgotPassword.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

}
