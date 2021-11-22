package com.example.myapplication.posts_list;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.EndlessRecyclerOnScrollListener;
import com.example.myapplication.R;
import com.example.myapplication.database.PostData;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener{
    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    final List<PostData> qpostsList = new ArrayList<>();
    private PostAdapter mAdapter;
    private int currentPage = 0;
    private ProgressBar mProgressBar;
    private static final int PAGE_SIZE = 10;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.posts);
        mProgressBar = root.findViewById(R.id.progress_bar_home);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAdapter = new PostAdapter(qpostsList);
        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                load_next_page();
            }
        });
        Log.d(TAG,"Finish oncreate");
        load_next_page();
        return root;
    }

    private void load_next_page(){
        Log.d(TAG,"Loading next page");
        currentPage++;
        mDatabase.child("posts")
                .limitToFirst(PAGE_SIZE)
                .startAt(currentPage*PAGE_SIZE)
                .orderByChild("title")
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG,"On Data Change");

                        if(!dataSnapshot.hasChildren()){
                            Toast.makeText(getContext(), "No more Posts", Toast.LENGTH_SHORT).show();
                            currentPage--;
                        }
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            PostData post = data.getValue(PostData.class);
                            qpostsList.add(post);
                            mAdapter.notifyDataSetChanged();
                        }
                        mProgressBar.setVisibility(RecyclerView.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG,"On Error");

                        mProgressBar.setVisibility(RecyclerView.GONE);
                    }
                });
        mProgressBar.setVisibility(RecyclerView.VISIBLE);
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
        //TODO: don't search in the db!!!!! that can take forever
    }
}
