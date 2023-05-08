package com.ptit.spotify.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.ptit.spotify.R;

public class SettingsActivity extends AppCompatActivity {
    private ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(view -> {
            finish();
        });
    }
}