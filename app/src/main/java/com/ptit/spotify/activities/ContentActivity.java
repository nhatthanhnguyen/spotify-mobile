package com.ptit.spotify.activities;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ptit.spotify.R;
import com.ptit.spotify.fragments.HomeFragment;
import com.ptit.spotify.fragments.LibraryFragment;
import com.ptit.spotify.fragments.SearchFragment;

public class ContentActivity extends AppCompatActivity {
    private final HomeFragment homeFragment = new HomeFragment();
    private final LibraryFragment libraryFragment = new LibraryFragment();
    private final SearchFragment searchFragment = new SearchFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                return true;
            }
            if (item.getItemId() == R.id.search) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, searchFragment).commit();
                return true;
            }
            if (item.getItemId() == R.id.library) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, libraryFragment).commit();
                return true;
            }
            return false;
        });

    }
}