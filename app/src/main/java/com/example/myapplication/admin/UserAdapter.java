package com.example.myapplication.admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.UserData;
import com.example.myapplication.utils.DownloadImageTask;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private final List<UserData> usersList;

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView user_name, user_email, user_type;
        ImageView user_image;
        ImageButton delete_user;

        UserViewHolder(View view) {
            super(view);
            user_name = itemView.findViewById(R.id.tv_user_name);
            user_email = itemView.findViewById(R.id.tv_user_email);
            user_type = itemView.findViewById(R.id.tv_user_type);
            user_image = itemView.findViewById(R.id.iv_user_image);
            delete_user = itemView.findViewById(R.id.ib_delete_user);
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
    public void onBindViewHolder(UserViewHolder holder, @SuppressLint("RecyclerView") int position) {
        UserData curr_user = usersList.get(position);
        holder.user_name.setText(curr_user.getName());
        holder.user_email.setText(curr_user.getEmail());
        holder.user_type.setText(curr_user.getUserType());
        new DownloadImageTask(holder.user_image).execute(curr_user.getProfile_image_url());

        //TODO: when admin click on a user, he can see UserDetails.
//        holder.itemView.setOnClickListener(v -> {
//            Context context = v.getContext();
//            Intent intent = new Intent(context, UserDetails.class);
//            intent.putExtra("post_data",curr_post);
//            context.startActivity(intent);
//        });
        holder.delete_user.setOnClickListener(v -> {
            DatabaseReference database;
            database = FirebaseDatabase.getInstance().getReference();
            //open a dialog and ask the user if he really wants to delete the post
            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
            alert.setTitle("Delete user");
            alert.setMessage("Are you sure you want to delete " + curr_user.getName() + "?");
            alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                database.child("UserData").child(curr_user.getUid()).removeValue();
                usersList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, usersList.size());
            });
            alert.setNegativeButton(android.R.string.no, (dialog, which) -> {
                dialog.cancel();
            });
            alert.show();
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}