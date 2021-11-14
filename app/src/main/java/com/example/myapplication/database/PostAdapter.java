package com.example.myapplication.database;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

// FirebaseRecyclerAdapter is a class provided by FirebaseUI.
// It provides functions to bind, adapt and show database contents in a Recycler View
public class PostAdapter extends FirebaseRecyclerAdapter<PostData, PostAdapter.PostViewHolder> {

    public PostAdapter(@NonNull FirebaseRecyclerOptions<PostData> options) {
        super(options);
    }

    // Function to bind the view in Card view(here "post.xml") with data in model class(here "Post.class")
    @SuppressLint("SetTextI18n")
    @Override
    protected void
    onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull PostData model) {

        // Add Post's fields from model class to appropriate view in Card view
        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        holder.starCount.setText(model.getStarCount()+" ⭐");
        int price = model.getPrice();
        holder.price.setText(price==0? "למסירה" : price+"₪");
    }

    // Function to tell the class about the Card view (here "post.xml") in which the data will be shown
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post, parent, false);
        return new PostViewHolder(view);
    }

    // Sub Class to create references of the views in Card view
    class PostViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, starCount, price;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tvTitle);
            description = itemView.findViewById(R.id.tvDescription);
            starCount = itemView.findViewById(R.id.tvStarCount);
            price = itemView.findViewById(R.id.tvPrice);
        }
    }
}
