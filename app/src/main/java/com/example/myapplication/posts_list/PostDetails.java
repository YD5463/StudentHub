package com.example.myapplication.posts_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Home;
import com.example.myapplication.R;
import com.example.myapplication.auth.ForgotPassword;
import com.example.myapplication.auth.Login;
import com.example.myapplication.database.PostData;
import com.example.myapplication.database.UserData;
import com.example.myapplication.utils.DownloadImageTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class PostDetails extends AppCompatActivity {
    static final String TAG = "PostDetails";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        init();

        setTitle("Post Details");
    }
    private void init(){
        PostData postData = (PostData) getIntent().getSerializableExtra("post_data");
        TextView title = findViewById(R.id.title_post_detail);
        TextView seller_name = findViewById(R.id.seller_name);
        TextView seller_rate = findViewById(R.id.seller_rate);
        ImageView phone_icon = findViewById(R.id.call_seller_icon);
        TextView description = findViewById(R.id.description);
        TextView price = findViewById(R.id.price);
        Button delete_post_button = (Button)findViewById(R.id.delete_post_btn);
        description.setText(postData.getDescription());
        price.setText(postData.getPrice()+"");
        title.setText(postData.getTitle());
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("UserData/"+postData.getUid()).get().addOnCompleteListener((task)->{
            if(task.isSuccessful()){
                UserData userData = task.getResult().getValue(UserData.class);
                if(userData==null)return;
                seller_name.setText(userData.getName());
                phone_icon.setOnClickListener(l->{
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+userData.phone_number));
                    startActivity(intent);
                });
                new DownloadImageTask(findViewById(R.id.seller_profile_image)).execute(userData.profile_image_url);
                if (firebaseUser.getUid().equals(postData.getUid())) {
                    delete_post_button.setVisibility(View.VISIBLE);
                    delete_post_button.setOnClickListener(v -> {
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("posts");
                        data.orderByChild("title").equalTo(postData.getTitle()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot data: dataSnapshot.getChildren()){
                                    data.getRef().removeValue();
                                }
                                Toast.makeText(PostDetails.this, "Post Successfully Deleted", Toast.LENGTH_SHORT).show();
                                startActivity( new Intent(PostDetails.this, Home.class));
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e(TAG, "onCancelled", error.toException());
                            }

                        });
                    });
                }
            }else{
                Log.e(TAG,"Error getting user data");
            }
        });

        if(postData.getImages().size() > 0){
            new DownloadImageTask(findViewById(R.id.post_image))
                    .execute(postData.getImages().get(0));
        }
    }

}