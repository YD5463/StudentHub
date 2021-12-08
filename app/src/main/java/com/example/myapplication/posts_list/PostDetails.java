package com.example.myapplication.posts_list;

import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.example.myapplication.database.PostData;
import com.example.myapplication.database.UserData;
import com.example.myapplication.utils.DownloadImageTask;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class PostDetails extends AppCompatActivity {
    static final String TAG = "PostDetails";
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
        title.setText(postData.getTitle());
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("UserData/"+postData.getUid()).get().addOnCompleteListener((task)->{
            if(task.isSuccessful()){
                UserData userData = task.getResult().getValue(UserData.class);
                seller_name.setText(userData.fullname);
                phone_icon.setOnClickListener(l->{
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+userData.phone_number));
                    startActivity(intent);
                });
                new DownloadImageTask(findViewById(R.id.seller_profile_image)).execute(userData.profile_image_url);
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