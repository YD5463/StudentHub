package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    static private final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button login_btn = findViewById(R.id.login_btn);
        Button signup_btn = findViewById(R.id.signup_btn);
        login_btn.setOnClickListener(v -> {
            Log.d(TAG,"on login handler");
            Intent login_intent = new Intent(getApplicationContext(),Login.class);
            startActivity(login_intent);
        });
        signup_btn.setOnClickListener(v->{
            Log.d(TAG,"on signup handler");
            Intent signup_intent = new Intent(getApplicationContext(),Signup.class);
            startActivity(signup_intent);
        });
    }
}