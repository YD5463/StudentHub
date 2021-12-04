package com.example.myapplication.my_profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.example.myapplication.auth.MainActivity;
import com.example.myapplication.database.UserData;
import com.example.myapplication.databinding.FragmentAccountBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountFragment extends Fragment{
    static final String TAG = "AccountFragment";
    private AccountViewModel accountViewModel;
    private FragmentAccountBinding binding;
    private AccountOption manage_users_option;

    private void logout(){
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0, 0);
        FirebaseAuth.getInstance().signOut();
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        TextView usernameView = (TextView)root.findViewById(R.id.user_name);
        TextView emailView = (TextView) root.findViewById(R.id.user_email);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        emailView.setText(user.getEmail());
        database.child("UserData/"+user.getUid()).get().addOnCompleteListener((task)->{
            if(task.isSuccessful()){
                UserData userData = task.getResult().getValue(UserData.class);
                assert userData != null;
                usernameView.setText(userData.fullname);
                if(userData.isAdmin){
                    manage_users_option.setVisibility(View.VISIBLE);
                }
            }else{
                Log.e(TAG,"Error getting user data");
            }
        });

        manage_users_option = root.findViewById(R.id.manage_user);
//        manage_users_option.setOnClickListener();
        AccountOption logout_option = root.findViewById(R.id.logout);
        logout_option.setOnClickListener((v)->logout());
        AccountOption my_posts_option = root.findViewById(R.id.my_posts);
//        my_posts_option.setOnClickListener(); //TODO

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}