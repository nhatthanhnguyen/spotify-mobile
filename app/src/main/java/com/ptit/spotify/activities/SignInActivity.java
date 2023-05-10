package com.ptit.spotify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.ptit.spotify.R;

public class SignInActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setControl();
        setAction();
    }

    private void setControl() {
        btnBack = findViewById(R.id.btnBack);
        btnSignIn = findViewById(R.id.buttonSignIn);
    }

    private void setAction() {
        btnBack.setOnClickListener(view -> {
            finish();
        });
        btnSignIn.setOnClickListener(view -> {
            Intent intent = new Intent(this, ContentActivity.class);
            startActivity(intent);
            finish();
        });
    }
}