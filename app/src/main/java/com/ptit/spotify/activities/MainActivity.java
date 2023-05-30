package com.ptit.spotify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.ptit.spotify.R;
import com.ptit.spotify.helper.SessionManager;

public class MainActivity extends AppCompatActivity {
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        session = new SessionManager(this);
        new Handler().postDelayed(() -> {
            Intent intent;
            if (session.isLoggedIn()) {
                intent = new Intent(this, ContentActivity.class);
            } else {
                intent = new Intent(this, StartActivity.class);
            }
            startActivity(intent);
            finish();
        }, 2000);
    }
}