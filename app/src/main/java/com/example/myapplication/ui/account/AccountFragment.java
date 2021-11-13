package com.example.myapplication.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentAccountBinding;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountFragment extends Fragment implements View.OnClickListener {

    private AccountViewModel accountViewModel;
    private FragmentAccountBinding binding;
    private Button btnMyPosts;
    private Button logoutBtn;
    private FirebaseAuth firebaseAuth;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView textView = binding.textNotifications;
        btnMyPosts = binding.btnMyPosts;
        btnMyPosts.setOnClickListener(this);
        accountViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // button for logout and initialing our button.
        logoutBtn = root.findViewById(R.id.idBtnLogout);
        // adding onclick listener for our logout button.
        logoutBtn.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

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
        if (v == btnMyPosts) {
            Navigation.findNavController(v).navigate(R.id.action_navigation_myAccount_to_myPostsFragment);
        } else if (v == logoutBtn) {
            // below line is for getting instance
            // for AuthUi and after that calling a
            // sign out method from Firebase.
            AuthUI.getInstance()
                    .signOut(getContext())
                    // after sign out is executed we are redirecting
                    // our user to MainActivity where our login flow is being displayed.
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            // below method is used after logout from device.
                            Toast.makeText(getContext(), "User Signed Out", Toast.LENGTH_SHORT).show();

                            // below line is to go to MainActivity via an intent.
                            Intent i = new Intent(getContext(), MainActivity.class);
                            startActivity(i);
                        }
                    });
        }
    }
}