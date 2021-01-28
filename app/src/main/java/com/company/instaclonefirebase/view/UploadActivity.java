package com.company.instaclonefirebase.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.company.instaclonefirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {

    private Bitmap selectedImage;
    private Uri selectedImageUri;
    private ImageView imageView;
    private EditText textComment;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String imageUrl;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        imageView = findViewById(R.id.imageView);
        textComment = findViewById(R.id.textComment);

        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = firebaseStorage.getReference();

    }

    public void upload(View view) {

        if (selectedImageUri != null) {
            String tempImageName = "images/" + UUID.randomUUID().toString().concat(".jpg");
            storageReference.child(tempImageName).putFile(selectedImageUri).addOnSuccessListener(snapshot -> {

                StorageReference newReference = FirebaseStorage.getInstance().getReference(tempImageName);
                newReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageUrl = uri.toString();
                    System.out.println(imageUrl);

                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    String userEmail = firebaseUser.getEmail();

                    String comment = textComment.getText().toString();

                    HashMap<String, Object> postData = new HashMap<>();
                    postData.put("email", userEmail);
                    postData.put("downloadurl", imageUrl);
                    postData.put("comment", comment);
                    postData.put("date", FieldValue.serverTimestamp());

                    firestore.collection("Posts").add(postData).addOnSuccessListener(documentReference -> {
                        Intent intent = new Intent(UploadActivity.this, FeedActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    });
                });

            }).addOnFailureListener(e -> Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show());
        }


    }

    public void selectImage(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // No Granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            //Granted
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery, 2);
        }
    }

    // İzin isteminin sonucu
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery, 2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // Resim seçildikten sonra yapılacak işlemler
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            // Image URI
            selectedImageUri = data.getData();

            try {
                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), selectedImageUri);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    imageView.setImageBitmap(selectedImage);
                } else {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    imageView.setImageBitmap(selectedImage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}