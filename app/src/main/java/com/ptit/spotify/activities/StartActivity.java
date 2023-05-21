package com.ptit.spotify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ptit.spotify.R;
import com.ptit.spotify.helper.SessionManager;

public class StartActivity extends AppCompatActivity {
    private Button btnRegister, btnSignIn;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setControl();
        setAction();
        session = new SessionManager(this);

        if(session.isLoggedIn()) {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setControl() {
        btnRegister = findViewById(R.id.buttonRegister);
        btnSignIn = findViewById(R.id.buttonSignIn);
    }

    private void setAction() {
        btnSignIn.setOnClickListener(view -> {
            Intent intent = new Intent(this, ContentActivity.class);
            startActivity(intent);
            finish();
        });
        btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
