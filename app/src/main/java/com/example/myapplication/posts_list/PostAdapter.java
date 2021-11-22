package com.example.myapplication.posts_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.PostData;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final List<PostData> postsList;

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, price;
        RatingBar starCount;

        PostViewHolder(View view) {
            super(view);
            title = itemView.findViewById(R.id.tvTitle);
            description = itemView.findViewById(R.id.tvDescription);
            starCount = itemView.findViewById(R.id.tvStarCount);
            price = itemView.findViewById(R.id.tvPrice);
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
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }
}