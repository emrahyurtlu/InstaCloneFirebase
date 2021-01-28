package com.company.instaclonefirebase.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.instaclonefirebase.R;
import com.company.instaclonefirebase.adapter.FeedRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {
    ArrayList<String> userEmailFromFB;
    ArrayList<String> userCommentFromFB;
    ArrayList<String> userImageFromFB;
    private RecyclerView rcView;
    private FeedRecyclerAdapter feedRecyclerAdapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        userEmailFromFB = new ArrayList<>();
        userCommentFromFB = new ArrayList<>();
        userImageFromFB = new ArrayList<>();

        feedRecyclerAdapter = new FeedRecyclerAdapter(userEmailFromFB, userCommentFromFB, userImageFromFB);

        rcView = findViewById(R.id.rcView);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        rcView.setAdapter(feedRecyclerAdapter);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getDataFromFirestore();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.insta_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add_post) {
            Intent intent = new Intent(FeedActivity.this, UploadActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.logout) {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        firebaseAuth.signOut();

        Intent intent = new Intent(FeedActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    public void getDataFromFirestore() {
        CollectionReference posts = firebaseFirestore.collection("Posts");

        posts.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(FeedActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }

            if (!value.isEmpty()) {
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    Map<String, Object> data = snapshot.getData();

                    String email = data.get("email").toString();
                    String comment = data.get("comment").toString();
                    String downloadUrl = data.get("downloadurl").toString();

                    userEmailFromFB.add(email);
                    userCommentFromFB.add(comment);
                    userImageFromFB.add(downloadUrl);

                    feedRecyclerAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}