package com.example.myapplication.my_profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.admin.ManageUsers;
import com.example.myapplication.auth.MainActivity;
import com.example.myapplication.business_entities.UserData;
import com.example.myapplication.utils.DownloadImageTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyProfileFragment extends Fragment{
    static final String TAG = "AccountFragment";
    private AccountOption manage_users_option;

    private void logout(){
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        (requireActivity()).overridePendingTransition(0, 0);
        FirebaseAuth.getInstance().signOut();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        TextView usernameView = root.findViewById(R.id.user_name);
        TextView emailView = root.findViewById(R.id.user_email);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        emailView.setText(user.getEmail());
        database.child("UserData/"+user.getUid()).get().addOnCompleteListener((task)->{
            if(task.isSuccessful()){
                UserData userData = task.getResult().getValue(UserData.class);
                assert userData != null;
                usernameView.setText(userData.getName());
                if(userData.isAdmin){
                    manage_users_option.setVisibility(View.VISIBLE);
                }
                new DownloadImageTask(root.findViewById(R.id.profile_image_my_account)).execute(userData.profile_image_url);
            }else{
                Log.e(TAG,"Error getting user data");
            }
        });

        manage_users_option = root.findViewById(R.id.manage_user);
        manage_users_option.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), ManageUsers.class);
            startActivity(i);
        });
        AccountOption logout_option = root.findViewById(R.id.logout);
        logout_option.setOnClickListener((v)->logout());
        AccountOption my_posts_option = root.findViewById(R.id.my_posts);
        my_posts_option.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MyPostsActivity.class);
            startActivity(intent);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}