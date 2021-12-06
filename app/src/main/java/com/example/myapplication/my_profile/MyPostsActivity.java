package com.example.myapplication.my_profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.database.PostData;
import com.example.myapplication.posts_list.PostAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyPostsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PostAdapter post_adapter;
    private DatabaseReference database;
    private List<PostData> filtered_list = new ArrayList<>();
    private String current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        database = FirebaseDatabase.getInstance().getReference();
        current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerView = findViewById(R.id.my_posts_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        post_adapter = new PostAdapter(filtered_list);
        recyclerView.setAdapter(post_adapter);
        fetchAllPosts();
    }

    private void fetchAllPosts() {
        //query all posts that uid is equal to the current user's uid
        database.child("posts").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PostData post = snapshot.getValue(PostData.class);
                    System.out.println(post);
                    assert post != null;
                    if(post.getUid().equals(current_user_id)) {
                        filtered_list.add(post);
                    }
                    runOnUiThread(()->post_adapter.notifyDataSetChanged());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}