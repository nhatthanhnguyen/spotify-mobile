package com.ptit.spotify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.ptit.spotify.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }
}