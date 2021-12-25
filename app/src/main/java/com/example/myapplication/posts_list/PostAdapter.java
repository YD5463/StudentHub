package com.example.myapplication.posts_list;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.PostData;
import com.example.myapplication.utils.DownloadImageTask;
import com.example.myapplication.utils.Utils;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private static final String TAG = "PostAdapter";
    private final List<PostData> postsList;
    private static final int MAX_DESCRIPTION_LEN = 30;
    private static final int MAX_TITLE_LEN = 10;

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

    public PostAdapter(List<PostData> postsList) {
        this.postsList = postsList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        PostData curr_post = postsList.get(position);
        holder.title.setText(Utils.shortenText(curr_post.getTitle(),MAX_TITLE_LEN));
        holder.description.setText(Utils.shortenText(curr_post.getDescription(),MAX_DESCRIPTION_LEN));
        holder.price.setText(String.valueOf(curr_post.getPrice()));
        holder.starCount.setRating(curr_post.getStarCount());
        List<String> images_urls = curr_post.getImages();
        if(images_urls!= null && images_urls.size() != 0){
            new DownloadImageTask(holder.image)
                    .execute(images_urls.get(0));

        }
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, PostDetails.class);
            intent.putExtra("post_data",curr_post);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }
}