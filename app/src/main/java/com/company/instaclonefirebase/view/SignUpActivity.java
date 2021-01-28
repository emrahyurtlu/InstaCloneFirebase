package com.company.instaclonefirebase.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.company.instaclonefirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailText;
    private EditText passwordText;

    private FirebaseAuth firebaseAuth;

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        firebaseAuth = FirebaseAuth.getInstance();

        redirectIfUserLoggedIn();

    }

    private void redirectIfUserLoggedIn() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null){
            Intent intent = new Intent(SignUpActivity.this, FeedActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void signInClicked(View view) {
        email = emailText.getText().toString();
        password = passwordText.getText().toString();

        if (!email.isEmpty() && !password.isEmpty()) {

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                Toast.makeText(getApplicationContext(), "Giriş yaptınız.", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(SignUpActivity.this, FeedActivity.class);
                startActivity(intent);
                // Buradaki finish() metodunun çağrılması tekrar SignUpActivity ye dönmemizi engelliyor.
                // ve back button a basılınca geri dönülmesi engelleniyor.
                finish();

            }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show());

        } else {
            Toast.makeText(getApplicationContext(), "Eposta ve şifre alanları boş olamaz.", Toast.LENGTH_LONG).show();
        }
    }

    public void signUpClicked(View view) {
        email = emailText.getText().toString();
        password = passwordText.getText().toString();

        if (!email.isEmpty() && !password.isEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                // Successful Auth
                Toast.makeText(SignUpActivity.this, "Kullanıcı oluşturuldu.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignUpActivity.this, FeedActivity.class);
                startActivity(intent);
                finish();

            }).addOnFailureListener(e -> {
                // Failure for unsuccessful Auth
                Toast.makeText(SignUpActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            });
        } else {
            Toast.makeText(getApplicationContext(), "Eposta ve şifre alanları boş olamaz.", Toast.LENGTH_LONG).show();
        }
    }
}