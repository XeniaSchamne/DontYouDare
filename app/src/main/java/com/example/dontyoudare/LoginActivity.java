package com.example.dontyoudare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    // Check ob der User bereits angemeldet ist
    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser != null){
            SendUserToMainActivity();
        }

    }

    private void SendUserToMainActivity() {
        Intent logIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(logIntent);
    }
}
