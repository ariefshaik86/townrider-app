package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        SharedPreferences prefs =
                getSharedPreferences("user", MODE_PRIVATE);

        // If already logged in → open home
        if (prefs.getBoolean("isLoggedIn", false)) {
            openHome();
            return;
        }

        checkPermission();
    }

    private void checkPermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100);

        } else {
            showPopup();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == 100) {
            showPopup(); // Whether allow or deny → continue
        }
    }

    private void showPopup() {
        LoginBottomSheet sheet = new LoginBottomSheet();
        sheet.setCancelable(true);
        sheet.show(getSupportFragmentManager(), "loginSheet");
    }

    public void openHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}