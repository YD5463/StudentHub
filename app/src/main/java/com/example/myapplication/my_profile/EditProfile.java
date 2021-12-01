package com.example.myapplication.my_profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;


public class EditProfile extends Fragment {


    public EditProfile() { }

    public static EditProfile newInstance() {
        EditProfile fragment = new EditProfile();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View binding = inflater.inflate(R.layout.fragment_edit_profile, container, false);
//        TextView textView = binding.;
//        accountViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return binding;
    }
}