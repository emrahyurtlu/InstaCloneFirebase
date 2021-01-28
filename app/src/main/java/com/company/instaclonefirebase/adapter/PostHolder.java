package com.company.instaclonefirebase.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.instaclonefirebase.R;

public class PostHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView userEmailText;
    TextView userCommentText;

    public PostHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.rc_row_image);
        userEmailText = itemView.findViewById(R.id.rc_row_user_email);
        userCommentText = itemView.findViewById(R.id.rc_row_user_comment);
    }

}
