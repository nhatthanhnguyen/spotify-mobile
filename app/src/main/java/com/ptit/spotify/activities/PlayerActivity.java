package com.ptit.spotify.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ptit.spotify.R;

public class PlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
    }
}