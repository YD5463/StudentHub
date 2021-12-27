package com.example.myapplication.posts_list;

import androidx.annotation.NonNull;

import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.Home;
import com.example.myapplication.R;
import com.example.myapplication.database.GPSCoordinates;
import com.example.myapplication.database.PostData;
import com.example.myapplication.database.UserData;
import com.example.myapplication.utils.DownloadImageTask;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class PostDetails extends FragmentActivity {
    static final String TAG = "PostDetails";
    private boolean isCurrentUserAdmin = false;
    MapView mMapView;
    private GoogleMap googleMap;

    private void setMarker(GPSCoordinates location){
        LatLng lng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(lng).title("Marker Title").snippet("Marker Description"));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(lng).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        mMapView = findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        init();
        setTitle("Post Details");
    }

    private void delete_post(String post_creation_time) {
        DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("posts");
        data.orderByChild("creation_date").equalTo(post_creation_time).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    data.getRef().removeValue();
                }
                Toast.makeText(PostDetails.this, "Post Successfully Deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PostDetails.this, Home.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled", error.toException());
            }
        });
    }

    private void fetch_additional_data(String uid, String post_creation_time) {
        TextView seller_name = findViewById(R.id.seller_name);
        ImageView phone_icon = findViewById(R.id.call_seller_icon);
        Button delete_post_button = (Button) findViewById(R.id.delete_post_btn);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("UserData/" + uid).get().addOnCompleteListener((task) -> {
            if (task.isSuccessful()) {
                UserData userData = task.getResult().getValue(UserData.class);
                if (userData == null) return;
                if (userData.isAdmin) {
                    isCurrentUserAdmin = true;
                }
                seller_name.setText(userData.getName());
                phone_icon.setOnClickListener(l -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + userData.phone_number));
                    startActivity(intent);
                });
                new DownloadImageTask(findViewById(R.id.seller_profile_image)).execute(userData.profile_image_url);
                assert firebaseUser != null;
                if (firebaseUser.getUid().equals(uid) || isCurrentUserAdmin) {
                    delete_post_button.setVisibility(View.VISIBLE);
                    delete_post_button.setOnClickListener(v -> {
                        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                        alert.setTitle(getApplicationContext().getString(R.string.delete_post_title));
                        alert.setMessage(getApplicationContext().getString(R.string.delete_post_question));
                        alert.setPositiveButton(android.R.string.yes, (dialog, which) -> delete_post(post_creation_time));
                        alert.setNegativeButton(android.R.string.no, (dialog, which) -> dialog.cancel());
                        alert.show();
                    });
                }
            } else {
                Log.e(TAG, "Error getting user data");
            }
        });
    }

    private void set_location_map(GPSCoordinates location){
        if(location == null){
            mMapView.setVisibility(View.GONE);
            return;
        }
        final GPSCoordinates finalLocation = location;
        mMapView.getMapAsync(mMap -> {
            googleMap = mMap;
            setMarker(finalLocation);
        });
    }
    @SuppressLint("SetTextI18n")
    private void init(){
        PostData postData = (PostData) getIntent().getSerializableExtra("post_data");
        set_location_map(postData.getSeller_location());
        TextView title = findViewById(R.id.title_post_detail);
        TextView description = findViewById(R.id.description);
        TextView postDate = findViewById(R.id.uploadDate);
        TextView price = findViewById(R.id.price);
        description.setText(postData.getDescription());
        price.setText("$"+postData.getPrice());
        title.setText(postData.getTitle());
        postDate.setText("Posted: "+postData.getCreation_date());
        fetch_additional_data(postData.getUid(),postData.getCreation_date());
        if(postData.getImages()!=null && postData.getImages().size() > 0){
            new DownloadImageTask(findViewById(R.id.post_image))
                    .execute(postData.getImages().get(0));
        }
    }

}