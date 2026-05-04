package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        loadSavedLocation();

        findViewById(R.id.fabCart).setOnClickListener(v -> {
            if (!isLoggedIn()) {
                showLoginPopup();
            } else {
                startActivity(new Intent(this, CartActivity.class));
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(android.R.id.content),
                (v, insets) -> {
                    int bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
                    v.setPadding(0, 0, 0, bottom);
                    return insets;
                });

        setupRecycler();
        setupBottomNav();
    }

    private boolean isLoggedIn() {
        return getSharedPreferences("user", MODE_PRIVATE)
                .getBoolean("isLoggedIn", false);
    }

    private void showLoginPopup() {
        LoginBottomSheet sheet = new LoginBottomSheet();
        sheet.show(getSupportFragmentManager(), "loginSheet");
    }

    private void setupRecycler() {

        RecyclerView recyclerView = findViewById(R.id.recyclerCategories);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        String url = "http://10.177.237.34/townride/get_main_categories.php";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {

                    try {

                        JSONArray array = new JSONArray(response);
                        List<Category> list = new ArrayList<>();

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject obj = array.getJSONObject(i);

                            int id = obj.getInt("id");
                            String name = obj.getString("name");
                            String image = obj.getString("image");

                            list.add(new Category(id, name, image));
                        }

                        CategoryAdapter adapter =
                                new CategoryAdapter(list);

                        recyclerView.setAdapter(adapter);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this,
                                "JSON Error",
                                Toast.LENGTH_SHORT).show();
                    }

                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this,
                            "Server Error",
                            Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(this).add(request);
    }
    private void setupBottomNav() {

        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true;
            }

            if (id == R.id.nav_menu) {
                startActivity(new Intent(this, MenuActivity.class));
                return true;
            }

            if (id == R.id.nav_orders) {

                if (!isLoggedIn()) {
                    showLoginPopup();
                    return false;
                }

                startActivity(new Intent(this, CartActivity.class));
                return true;
            }

            return false;
        });
    }

    private void loadSavedLocation() {

        SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);

        String latStr = prefs.getString("lat", null);
        String lngStr = prefs.getString("lng", null);

        if (latStr == null || lngStr == null) return;

        double lat = Double.parseDouble(latStr);
        double lng = Double.parseDouble(lngStr);

        new Thread(() -> {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);

                if (addresses != null && !addresses.isEmpty()) {

                    Address address = addresses.get(0);

                    String tempTown = address.getSubAdminArea();
                    if (tempTown == null) tempTown = address.getLocality();
                    if (tempTown == null) tempTown = address.getAdminArea();

                    String tempFullAddress = address.getAddressLine(0);

                    final String town = tempTown;
                    final String fullAddress = tempFullAddress;

                    runOnUiThread(() -> {

                        TextView tvTown = findViewById(R.id.tvTownName);
                        TextView tvAddress = findViewById(R.id.tvFullAddress);
                        TextView tvDeliverTown = findViewById(R.id.tvDeliverTown);
                        TextView tvDeliverAddress = findViewById(R.id.tvDeliverAddress);

                        if (town != null) {
                            tvTown.setText(town);
                            tvDeliverTown.setText(town);
                        }

                        if (fullAddress != null) {
                            tvAddress.setText(fullAddress);
                            tvDeliverAddress.setText(fullAddress);
                        }
                    });
                }

            } catch (Exception ignored) {
            }
        }).start();
    }
}