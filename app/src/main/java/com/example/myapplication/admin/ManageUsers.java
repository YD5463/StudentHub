package com.example.myapplication.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.business_entities.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageUsers extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter user_adapter;
    private DatabaseReference database;
    private List<UserData> filtered_list = new ArrayList<>();
    private String current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        database = FirebaseDatabase.getInstance().getReference();
        current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerView = findViewById(R.id.manage_user_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        user_adapter = new UserAdapter(filtered_list);
        recyclerView.setAdapter(user_adapter);
        fetchAllPosts();
    }

    private void fetchAllPosts() {
        //query all posts that uid is equal to the current user's uid
        database.child("UserData").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserData user = snapshot.getValue(UserData.class);
                    System.out.println(user);
                    assert user != null;
                    //TODO: check if user is admin
//                    if(user.getUid().equals(current_user_id)) {
                        filtered_list.add(user);
//                    }
                    runOnUiThread(()->user_adapter.notifyDataSetChanged());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}