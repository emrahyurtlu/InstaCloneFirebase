package com.company.instaclonefirebase.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.instaclonefirebase.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<PostHolder> {

    private final ArrayList<String> emails;
    private final ArrayList<String> comments;
    private final ArrayList<String> imageUrls;

    public FeedRecyclerAdapter(ArrayList<String> emails, ArrayList<String> comments, ArrayList<String> imageUrls) {
        this.emails = emails;
        this.comments = comments;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row, parent, false);

        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.userEmailText.setText(emails.get(position));
        holder.userCommentText.setText(comments.get(position));

        Picasso.get().load(imageUrls.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return emails.size();
    }
}
