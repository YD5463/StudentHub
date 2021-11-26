package com.example.myapplication.posts_list;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.PostData;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private static final String TAG = "PostAdapter";
    private final List<PostData> postsList;

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, price;
        ImageView image;
        RatingBar starCount;

        PostViewHolder(View view) {
            super(view);
            title = itemView.findViewById(R.id.tvTitle);
            description = itemView.findViewById(R.id.tvDescription);
            starCount = itemView.findViewById(R.id.tvStarCount);
            price = itemView.findViewById(R.id.tvPrice);
            image = itemView.findViewById(R.id.imageView);
        }
    }

    PostAdapter(List<PostData> postsList) {
        this.postsList = postsList;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post, parent, false);

        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        PostData post = postsList.get(position);
        holder.title.setText(post.getTitle());
        holder.description.setText(post.getDescription());
        holder.price.setText(String.valueOf(post.getPrice()));
        holder.starCount.setRating(post.getStarCount());
        List<String> images_urls = post.getImages();
        if(images_urls.size() != 0){
            Executors.newSingleThreadExecutor().execute(()->{
            try{
                URL url = new URL(images_urls.get(0));
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                holder.image.setImageBitmap(bmp);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e(TAG,"Error");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }
}