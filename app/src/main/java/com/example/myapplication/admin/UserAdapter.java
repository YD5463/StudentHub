package com.example.myapplication.admin;

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
import com.example.myapplication.database.UserData;
import com.example.myapplication.utils.DownloadImageTask;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private final List<UserData> usersList;

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView user_name, user_email, user_type;
        ImageView user_image;

        UserViewHolder(View view) {
            super(view);
            user_name = itemView.findViewById(R.id.tv_user_name);
            user_email = itemView.findViewById(R.id.tv_user_email);
            user_type = itemView.findViewById(R.id.tv_user_type);
            user_image = itemView.findViewById(R.id.iv_user_image);
        }
    }

    public UserAdapter(List<UserData> postsList) {
        this.usersList = postsList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        UserData curr_user = usersList.get(position);
        holder.user_name.setText(curr_user.getName());
        holder.user_email.setText(curr_user.getEmail());
        holder.user_type.setText(curr_user.getUserType());
        new DownloadImageTask(holder.user_image).execute(curr_user.getProfile_image_url());

//        String images_urls = curr_user.getProfile_image_url();
//        if(images_urls != null) {
//            new DownloadImageTask(holder.user_image).execute(images_urls);
//        }
//        holder.itemView.setOnClickListener(v -> {
//            Context context = v.getContext();
//            Intent intent = new Intent(context, PostDetails.class);
//            intent.putExtra("post_data",curr_post);
//            context.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}