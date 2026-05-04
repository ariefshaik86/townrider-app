package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GroceriesActivity extends AppCompatActivity {

    RecyclerView recyclerGroceries,
            recyclerGroceryCategories,
            recyclerGroceryOffers,
            recyclerBestGroceries;

    ViewPager2 bannerSlider;
    LinearLayout topContent;
    EditText etSearch;

    GroceryAdapter groceryAdapter;
    GroceryCategoryAdapter categoryAdapter;

    List<GroceryItem> groceryList = new ArrayList<>();
    List<GroceryCategory> categoryList = new ArrayList<>();

    boolean isCategoryMode = false;

    private static final String BASE_URL =
            "http://10.177.237.34/townride/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries);

        bannerSlider = findViewById(R.id.bannerSliderGrocery);
        recyclerGroceries = findViewById(R.id.recyclerGroceries);
        recyclerGroceryCategories = findViewById(R.id.recyclerGroceryCategories);
        recyclerGroceryOffers = findViewById(R.id.recyclerGroceryOffers);
        recyclerBestGroceries = findViewById(R.id.recyclerBestGroceries);
        etSearch = findViewById(R.id.etSearchGrocery);
        topContent = findViewById(R.id.topContent);

        recyclerGroceries.setLayoutManager(new LinearLayoutManager(this));
        recyclerGroceryCategories.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerGroceryOffers.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerBestGroceries.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        groceryAdapter = new GroceryAdapter(groceryList);
        recyclerGroceries.setAdapter(groceryAdapter);

        categoryAdapter = new GroceryCategoryAdapter(categoryList,
                categoryId -> {
                    isCategoryMode = true;
                    topContent.setVisibility(View.GONE);
                    loadGroceriesByCategory(categoryId);
                });

        recyclerGroceryCategories.setAdapter(categoryAdapter);

        setupSearch();
        loadCategories();
        loadBanners();
        loadOffers();
        loadBestRated();
    }

    // ================= LOAD CATEGORIES =================
    private void loadCategories() {

        String url = BASE_URL + "get_grocery_categories.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {

                    categoryList.clear();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            int id = obj.getInt("id");
                            String name = obj.getString("name");
                            String image = obj.getString("image");

                            categoryList.add(new GroceryCategory(id, name, image));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    categoryAdapter.notifyDataSetChanged();

                    if (!categoryList.isEmpty()) {
                        loadGroceriesByCategory(categoryList.get(0).getId());
                    }
                },
                error -> error.printStackTrace()
        );

        queue.add(request);
    }

    // ================= LOAD GROCERIES BY CATEGORY =================
    private void loadGroceriesByCategory(int categoryId) {

        String url = BASE_URL +
                "get_items_by_category.php?category_id=" + categoryId;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {

                    groceryList.clear();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            String name = obj.getString("name");
                            int price = obj.getInt("price");
                            String image = obj.getString("image");

                            groceryList.add(new GroceryItem(name, price, image));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    groceryAdapter.notifyDataSetChanged();
                },
                error -> error.printStackTrace()
        );

        queue.add(request);
    }

    // ================= SEARCH =================
    private void setupSearch() {

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().isEmpty()) {
                    if (!isCategoryMode) {
                        topContent.setVisibility(View.VISIBLE);
                    }
                } else {
                    topContent.setVisibility(View.GONE);
                }

                filter(s.toString());
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    private void filter(String text) {

        List<GroceryItem> filtered = new ArrayList<>();

        for (GroceryItem item : groceryList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filtered.add(item);
            }
        }

        recyclerGroceries.setAdapter(new GroceryAdapter(filtered));
    }

    // ================= LOAD BANNERS =================
    private void loadBanners() {

        String url = BASE_URL + "get_grocery_banners.php";

        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {
                    List<BannerItem> list = new ArrayList<>();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            list.add(new BannerItem(obj.getString("image")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    bannerSlider.setAdapter(new BannerAdapter(list));
                },
                error -> error.printStackTrace());

        Volley.newRequestQueue(this).add(request);
    }

    // ================= LOAD OFFERS =================
    private void loadOffers() {

        String url = BASE_URL + "get_grocery_offers.php";

        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {

                    List<GroceryOffer> list = new ArrayList<>();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            list.add(new GroceryOffer(
                                    obj.getString("name"),
                                    obj.getString("original_price"),
                                    obj.getString("offer_price"),
                                    obj.getString("image")
                            ));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    recyclerGroceryOffers.setAdapter(
                            new GroceryOfferAdapter(list));

                },
                error -> error.printStackTrace());

        Volley.newRequestQueue(this).add(request);
    }

    // ================= LOAD BEST RATED =================
    private void loadBestRated() {

        String url = BASE_URL + "get_best_rated_groceries.php";

        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {

                    List<BestRatedGrocery> list = new ArrayList<>();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            list.add(new BestRatedGrocery(
                                    obj.getString("name"),
                                    obj.getString("price"),
                                    obj.getString("image"),
                                    obj.getString("rating")
                            ));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    recyclerBestGroceries.setAdapter(
                            new BestRatedGroceryAdapter(list));

                },
                error -> error.printStackTrace());

        Volley.newRequestQueue(this).add(request);
    }

    // ================= BACK BUTTON =================
    @Override
    public void onBackPressed() {

        if (isCategoryMode) {
            isCategoryMode = false;
            topContent.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

}