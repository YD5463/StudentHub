package com.example.myapplication.posts_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.PostAdapter;
import com.example.myapplication.database.PostData;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener{

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    PostAdapter adapter; // Create Object of the Adapter class
    DatabaseReference db; // Create object of the Firebase Realtime Database

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);

        View root = binding.getRoot();

        // Create a instance of the database and get its reference
        db = FirebaseDatabase.getInstance().getReference().child("posts");

        recyclerView = root.findViewById(R.id.posts);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // It is a class provide by the FirebaseUI to make a query in the database to fetch appropriate data
        FirebaseRecyclerOptions<PostData> options = new FirebaseRecyclerOptions.Builder<PostData>()
                .setQuery(db, PostData.class)
                .build();
        // Connecting object of required Adapter class to the Adapter class itself
        adapter = new PostAdapter(options);
        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);

        return root;
    }

    // Function to tell the app to start getting data from database on starting of the activity
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting data from database on stopping of the activity
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        // Add the new menu items
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) { return false; }
    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) { return false; }

    /* When the user starts to type in the search section it filters the posts by title */
    @Override
    public boolean onQueryTextSubmit(String query) {
        processSearch(query);
        return false;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        processSearch(newText);
        return false;
    }

    /* Fetch filtered posts from firebase */
    private void processSearch(String s) {
        FirebaseRecyclerOptions<PostData> options =
                new FirebaseRecyclerOptions.Builder<PostData>()
                        .setQuery(db.orderByChild("title").startAt(s).endAt(s+"\uf8ff"), PostData.class)
                        .build();

        adapter = new PostAdapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}
