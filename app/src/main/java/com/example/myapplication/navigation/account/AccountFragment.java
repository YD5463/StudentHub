package com.example.myapplication.navigation.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentAccountBinding;
import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment implements View.OnClickListener {

    private AccountViewModel accountViewModel;
    private FragmentAccountBinding binding;
    private Button btnMyPosts;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView textView = binding.textNotifications;
        btnMyPosts = binding.btnMyPosts;
        btnMyPosts.setOnClickListener(this);
        accountViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        Button logout = root.findViewById(R.id.logout);
        logout.setOnClickListener(l->{
            Intent i = new Intent(getActivity(), MainActivity.class);
            startActivity(i);
            ((Activity) getActivity()).overridePendingTransition(0, 0);
            FirebaseAuth.getInstance().signOut();

        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {

        /*When the user clicks on the "My Posts" button in this fragment it navigates to MyPostsFragment*/
        if(v == btnMyPosts){
            Navigation.findNavController(v).navigate(R.id.action_navigation_myAccount_to_myPostsFragment);
        }
    }
}