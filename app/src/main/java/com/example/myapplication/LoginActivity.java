package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.google.android.gms.location.*;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText etPhone, etPassword;
    Button btnLogin;

    FusedLocationProviderClient fusedLocationClient;

    String BASE_URL = "http://10.177.237.34/townride/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etPhone = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        ImageView btnBack = findViewById(R.id.btnBack);
        TextView tvSignup = findViewById(R.id.tvSignup);

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        });

        tvSignup.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });

        // Ask Location Permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            getLocation();
        }

        btnLogin.setOnClickListener(v -> login());
    }

    // ================== LOCATION FUNCTION ==================

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {

                    if (location != null) {

                        double lat = location.getLatitude();
                        double lng = location.getLongitude();

                        SharedPreferences prefs =
                                getSharedPreferences("app", MODE_PRIVATE);

                        prefs.edit()
                                .putString("lat", String.valueOf(lat))
                                .putString("lng", String.valueOf(lng))
                                .apply();

                        Toast.makeText(this,
                                "Location Detected",
                                Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(this,
                                "Unable to detect location. Turn ON GPS.",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == 1 &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            getLocation();
        }
    }

    // ================== LOGIN FUNCTION ==================

    private void login() {

        String url = BASE_URL + "login.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.getString("status").equals("success")) {

                            SharedPreferences prefs =
                                    getSharedPreferences("user", MODE_PRIVATE);

                            prefs.edit()
                                    .putBoolean("isLoggedIn", true)
                                    .putInt("user_id", obj.getInt("user_id"))
                                    .putString("name", obj.getString("name"))
                                    .apply();

                            startActivity(new Intent(this, MainActivity.class));
                            finish();

                        } else {
                            Toast.makeText(this,
                                    obj.getString("message"),
                                    Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this,
                                "Server error",
                                Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this,
                            "Network error",
                            Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                SharedPreferences prefs =
                        getSharedPreferences("app", MODE_PRIVATE);

                String lat = prefs.getString("lat", "");
                String lng = prefs.getString("lng", "");

                Map<String, String> params = new HashMap<>();
                params.put("phone", etPhone.getText().toString().trim());
                params.put("password", etPassword.getText().toString().trim());
                params.put("lat", lat);
                params.put("lng", lng);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}