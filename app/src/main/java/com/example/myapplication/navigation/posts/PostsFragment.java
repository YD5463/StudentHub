package com.example.myapplication.navigation.posts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.databinding.FragmentNewPostBinding;

public class PostsFragment extends Fragment {

    private PostsViewModel postsViewModel;
    private FragmentNewPostBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        postsViewModel = new ViewModelProvider(this).get(PostsViewModel.class);

        binding = FragmentNewPostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNewPostTitle;
        postsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}