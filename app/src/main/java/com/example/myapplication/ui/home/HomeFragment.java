package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.database.Product;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobsandgeeks.saripaar.annotation.Max;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Optional;

import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private FirebaseAuth mAuth;

    private List<ImageView> images;

    @NotEmpty()
    private EditText title;
    @Optional()
    private EditText description;
    @NotEmpty()@Min(0) @Max(50000)
    private EditText price;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button btn = root.findViewById(R.id.add_post_btn);
        btn.setOnClickListener(this::onSubmit);
        return root;
    }
    void onSubmit(View v){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child("products").push().getKey();
        Product post = new Product(title.getText().toString(),description.getText().toString(),
                Integer.parseInt(price.getText().toString()),key);
        Map<String, Object> postValues = post.toMap();
        mDatabase.child("/posts/" + key).updateChildren(postValues);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}