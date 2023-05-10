package com.ptit.spotify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ptit.spotify.R;

public class StartActivity extends AppCompatActivity {
    private Button btnRegister, btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setControl();
        setAction();
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
