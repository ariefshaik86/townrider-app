package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ComingSoon extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming_soon);

        // Floating Cart
        findViewById(R.id.fabCart).setOnClickListener(v -> {
            if (!isLoggedIn()) {
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                startActivity(new Intent(this, CartActivity.class));
            }
        });

        // Bottom Navigation
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            }

            if (id == R.id.nav_menu) {
                startActivity(new Intent(this, MenuActivity.class));
                return true;
            }

            if (id == R.id.nav_orders) {
                startActivity(new Intent(this, CartActivity.class));
                return true;
            }

            return false;
        });
    }

    private boolean isLoggedIn() {
        return getSharedPreferences("user", MODE_PRIVATE)
                .getBoolean("isLoggedIn", false);
    }
}