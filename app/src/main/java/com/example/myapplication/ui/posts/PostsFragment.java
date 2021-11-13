package com.example.myapplication.ui.posts;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.Utils;
import com.example.myapplication.auth.Signup;
import com.example.myapplication.database.Product;
import com.example.myapplication.databinding.FragmentNewPostBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobsandgeeks.saripaar.annotation.Max;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Optional;

import java.util.List;
import java.util.Map;

public class PostsFragment extends Fragment {

    private PostsViewModel postsViewModel;
    private FragmentNewPostBinding binding;


    private List<ImageView> images;

    @NotEmpty()
    private EditText title;
    @Optional()
    private EditText description;
    @NotEmpty()@Min(0) @Max(50000)
    private EditText price;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        postsViewModel = new ViewModelProvider(this).get(PostsViewModel.class);

        binding = FragmentNewPostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init(root);
        Button btn = root.findViewById(R.id.add_post_btn);
        btn.setOnClickListener(this::onSubmit);
        return root;
    }
    void init(View root){
        title = root.findViewById(R.id.post_title);
        description = root.findViewById(R.id.post_description);
        price = root.findViewById(R.id.post_price);
    }
    void onSubmit(View v){
        Context context = getContext();
        ProgressDialog mDialog = new ProgressDialog(context);
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(true);
        mDialog.show();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child("products").push().getKey();
        Product post = new Product(title.getText().toString(),description.getText().toString(),
                Integer.parseInt(price.getText().toString()),key);
        Map<String, Object> postValues = post.toMap();
        mDatabase.child("/posts/" + key).updateChildren(postValues).
                addOnCompleteListener(task->{
                    mDialog.cancel();
                    if(task.isSuccessful()){
                        cleanForm();
                        Utils.hideKeyboardFrom(context,v);
                        Toast.makeText(context, "Post Added Successfully.",
                                Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Add Post Failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    void cleanForm(){
        title.setText("");
        description.setText("");
        price.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}