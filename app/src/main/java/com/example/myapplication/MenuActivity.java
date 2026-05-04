package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    TextView btnLogout;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnLogout = findViewById(R.id.btnLogout);
        btnLogin = findViewById(R.id.btnLoginSignup);

        updateProfileUI();
        setupBottomNav();
        setupCartClick();
        setupLogout();
        setupProtectedMenu();
    }

    // ================= PROFILE UI =================

    private void updateProfileUI() {

        TextView tvName = findViewById(R.id.tvUserName);
        TextView tvEmail = findViewById(R.id.tvUserEmail);

        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {

            String name = prefs.getString("name", "User");

            tvName.setText(name);
            tvEmail.setText("Welcome back!");
            btnLogin.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);

        } else {

            tvName.setText("Guest User");
            tvEmail.setText("Login to enjoy full features");
            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);

            btnLogin.setOnClickListener(v ->
                    startActivity(new Intent(MenuActivity.this, SignupActivity.class)));
        }
    }

    // ================= CART =================

    private void setupCartClick() {

        findViewById(R.id.fabCart).setOnClickListener(v -> {

            if (!isLoggedIn()) {
                startActivity(new Intent(this, SignupActivity.class));
            } else {
                startActivity(new Intent(this, CartActivity.class));
            }
        });
    }

    // ================= LOGOUT =================

    private void setupLogout() {

        btnLogout.setOnClickListener(v -> {

            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        SharedPreferences prefs =
                                getSharedPreferences("user", MODE_PRIVATE);

                        prefs.edit().clear().apply();

                        Intent intent = new Intent(this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    // ================= PROTECTED MENU =================

    private void setupProtectedMenu() {

        findViewById(R.id.btnEditProfile).setOnClickListener(v -> {

            if (!isLoggedIn()) {
                startActivity(new Intent(this, SignupActivity.class));
            } else {
                startActivity(new Intent(this, EditProfileActivity.class));
            }
        });

        findViewById(R.id.btnMyAddress).setOnClickListener(v -> {

            if (!isLoggedIn()) {
                startActivity(new Intent(this, SignupActivity.class));
            } else {
                startActivity(new Intent(this, AddressActivity.class));
            }
        });
    }

    // ================= BOTTOM NAV =================

    private void setupBottomNav() {

        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_menu);

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            }

            if (id == R.id.nav_menu) {
                return true;
            }

            return false;
        });
    }

    // ================= SESSION CHECK =================

    private boolean isLoggedIn() {
        return getSharedPreferences("user", MODE_PRIVATE)
                .getBoolean("isLoggedIn", false);
    }
}