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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.utils.RecyclerOnScrollListener;
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
    private RecyclerView recycler_view;
    private DatabaseReference database;
    final List<PostData> posts_list = new ArrayList<>();
    final List<PostData> filtered_list = new ArrayList<>();
    private PostAdapter post_adapter;
    private ProgressBar progress_bar;
    private boolean is_list_ends = false;
    private static final int PAGE_SIZE = 10;

    private String curr_query_search = "";
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        View root = binding.getRoot();

        recycler_view = root.findViewById(R.id.posts);
        progress_bar = root.findViewById(R.id.progress_bar_home);
        database = FirebaseDatabase.getInstance().getReference();

        init_list();
        return root;
    }

    private void init_list(){
        post_adapter = new PostAdapter(filtered_list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(post_adapter);
        //TODO: find replacement for setOnScrollListener
        recycler_view.setOnScrollListener(new RecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                load_next_page(current_page);
            }
            @Override
            public boolean isListEnds(){
                return is_list_ends;
            }
        });
        load_next_page(0);
    }
    private void load_next_page(int curr_page){
        //TODO: check network connection
        Log.d(TAG,"getting next page number: "+curr_page);
        database.child("posts")
                .limitToFirst(PAGE_SIZE)
                .startAt(curr_page*PAGE_SIZE)
                .orderByChild("title") //TODO: change to creation time
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.hasChildren()){
                            Toast.makeText(getContext(), "No more Posts", Toast.LENGTH_SHORT).show();
                        }
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            PostData post = data.getValue(PostData.class);
                            if(!posts_list.isEmpty() && post.getTitle().equals(posts_list.get(0).getTitle())){
                                progress_bar.setVisibility(RecyclerView.GONE);
                                is_list_ends = true;
                                return;
                            }
                            posts_list.add(post);
                            getActivity().runOnUiThread(()->post_adapter.notifyDataSetChanged());
                        }
                        processSearch(curr_query_search);
                        progress_bar.setVisibility(RecyclerView.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG,"onCancelled: "+databaseError.getMessage());
                        progress_bar.setVisibility(RecyclerView.GONE);
                    }
                });
        progress_bar.setVisibility(RecyclerView.VISIBLE);
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
    /**
     *
     * @param s search query
     */
    private void processSearch(String s) {
        Log.d(TAG,"Searching: "+s);
        curr_query_search = s;
        if(s.isEmpty()){ // deep copy original list(without deep coping postData)
            filtered_list.addAll(posts_list);
        }
        else{
            filtered_list.clear();
            for(PostData post : posts_list){
                if(post.getTitle().contains(s) || post.getDescription().contains(s)){
                    filtered_list.add(post);
                }
            }
        }
    }
}
