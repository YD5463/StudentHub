package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button login_btn = (Button) findViewById(R.id.login_btn);
        Button signup_btn = (Button) findViewById(R.id.signup_btn);
        login_btn.setOnClickListener(v -> {
            Log.d("Login page","im here");
        });
        signup_btn.setOnClickListener(v->{
            Log.d("Login page","im here 2");
        });
    }
}