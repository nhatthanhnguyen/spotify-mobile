package com.ptit.spotify.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.ptit.spotify.R;

public class RegisterActivity extends AppCompatActivity {
    private ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setControl();
        setAction();
    }

    void setControl() {
        btnBack = findViewById(R.id.btnBack);
    }

    void setAction() {
        btnBack.setOnClickListener(view -> {
            finish();
        });
    }
}