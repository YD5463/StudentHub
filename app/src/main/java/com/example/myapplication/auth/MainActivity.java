package com.example.myapplication.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.myapplication.Home;
import com.example.myapplication.R;
import com.example.myapplication.auth.Login;
import com.example.myapplication.auth.Signup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    static private final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent goto_home = new Intent(getApplicationContext(), Home.class);
            startActivity(goto_home);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
        Button login_btn = findViewById(R.id.login_btn);
        Button signup_btn = findViewById(R.id.signup_btn);
        login_btn.setOnClickListener(v -> {
            Log.d(TAG,"on login handler");
            Intent login_intent = new Intent(getApplicationContext(), Login.class);
            startActivity(login_intent);
        });
        signup_btn.setOnClickListener(v->{
            Log.d(TAG,"on signup handler");
            Intent signup_intent = new Intent(getApplicationContext(), Signup.class);
            startActivity(signup_intent);
        });
    }
}