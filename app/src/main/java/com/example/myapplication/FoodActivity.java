package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FoodActivity extends AppCompatActivity {

    RecyclerView recyclerFood, recyclerCategories, recyclerOffers, recyclerBest;
    ViewPager2 bannerSlider;
    LinearLayout topContent;
    EditText etSearch;

    FoodAdapter foodAdapter;
    FoodCategoryAdapter categoryAdapter;

    List<FoodItem> fullList = new ArrayList<>();
    List<FoodCategory> categoryList = new ArrayList<>();

    Handler sliderHandler = new Handler(Looper.getMainLooper());
    Runnable sliderRunnable;

    private static final String BASE_URL = "http://10.177.237.34/townride/";

    boolean isCategoryMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        bannerSlider = findViewById(R.id.bannerSlider);
        recyclerFood = findViewById(R.id.recyclerFood);
        recyclerCategories = findViewById(R.id.recyclerCategories);
        recyclerOffers = findViewById(R.id.recyclerOffers);
        recyclerBest = findViewById(R.id.recyclerBest);
        etSearch = findViewById(R.id.etSearch);
        topContent = findViewById(R.id.topContent);

        findViewById(R.id.fabCart).setOnClickListener(v ->
                startActivity(new Intent(FoodActivity.this, CartActivity.class)));

        recyclerFood.setLayoutManager(new LinearLayoutManager(this));
        recyclerCategories.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerOffers.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerBest.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        foodAdapter = new FoodAdapter(fullList);
        recyclerFood.setAdapter(foodAdapter);

        categoryAdapter = new FoodCategoryAdapter(categoryList, categoryId -> {
            isCategoryMode = true;
            topContent.setVisibility(View.GONE);
            loadItems(categoryId);
        });

        recyclerCategories.setAdapter(categoryAdapter);

        setupSearch();
        loadCategories();
        loadBanners();
        loadSpecialOffers();
        loadBestReviewed();
    }

    // ================= LOAD CATEGORIES =================
    private void loadCategories() {

        String url = BASE_URL + "get_categories.php";

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

                            categoryList.add(new FoodCategory(id, name, image));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    categoryAdapter.notifyDataSetChanged();

                    if (!categoryList.isEmpty()) {
                        loadItems(categoryList.get(0).getId());
                    }
                },
                error -> error.printStackTrace()
        );

        queue.add(request);
    }

    // ================= LOAD ITEMS =================
    private void loadItems(int categoryId) {

        String url = BASE_URL + "get_items.php?category_id=" + categoryId;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {

                    fullList.clear();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            String name = obj.getString("name");
                            int price = obj.getInt("price");
                            String image = obj.getString("image");

                            fullList.add(new FoodItem(name, price, image));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    foodAdapter.notifyDataSetChanged();
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

        List<FoodItem> filteredList = new ArrayList<>();

        for (FoodItem item : fullList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        recyclerFood.setAdapter(new FoodAdapter(filteredList));
    }

    // ================= LOAD BANNERS =================
    private void loadBanners() {

        String url = BASE_URL + "get_banners.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {

                    List<BannerItem> bannerList = new ArrayList<>();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            String image = obj.getString("image");
                            bannerList.add(new BannerItem(image));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    BannerAdapter adapter = new BannerAdapter(bannerList);
                    bannerSlider.setAdapter(adapter);

                    if (!bannerList.isEmpty()) {

                        sliderRunnable = new Runnable() {
                            @Override
                            public void run() {

                                int next = bannerSlider.getCurrentItem() + 1;

                                if (next >= bannerList.size()) {
                                    next = 0;
                                }

                                bannerSlider.setCurrentItem(next, true);
                                sliderHandler.postDelayed(this, 3000);
                            }
                        };

                        sliderHandler.postDelayed(sliderRunnable, 3000);
                    }
                },
                error -> error.printStackTrace()
        );

        queue.add(request);
    }

    // ================= BACK BUTTON LOGIC =================
    @Override
    public void onBackPressed() {

        if (isCategoryMode) {
            isCategoryMode = false;
            topContent.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sliderRunnable != null) {
            sliderHandler.removeCallbacks(sliderRunnable);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sliderRunnable != null) {
            sliderHandler.postDelayed(sliderRunnable, 3000);
        }
    }
    private void loadSpecialOffers() {

        String url = BASE_URL + "get_special_offers.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {

                    List<SpecialOffer> offerList = new ArrayList<>();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            String name = obj.getString("name");
                            String image = obj.getString("image");
                            String original = obj.getString("original_price");
                            String app = obj.getString("app_price");

                            offerList.add(new SpecialOffer(name, image, original, app));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    recyclerOffers.setAdapter(
                            new SpecialOfferAdapter(offerList));

                },
                error -> error.printStackTrace()
        );

        queue.add(request);
    }
    private void loadBestReviewed() {

        String url = BASE_URL + "get_best_reviewed.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {

                    List<BestReviewed> list = new ArrayList<>();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            String name = obj.getString("name");
                            String image = obj.getString("image");
                            double price = obj.getDouble("price");
                            double rating = obj.getDouble("rating");

                            list.add(new BestReviewed(name, image, price, rating));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    recyclerBest.setAdapter(
                            new BestReviewedAdapter(list));

                },
                error -> error.printStackTrace()
        );

        queue.add(request);
    }
}