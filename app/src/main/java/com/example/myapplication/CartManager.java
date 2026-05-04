package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static List<FoodItem> cartList = new ArrayList<>();
    private static final String PREF_NAME = "cart_pref";
    private static final String KEY_CART = "cart_items";

    // ✅ Add item
    public static void addToCart(FoodItem item, Context context) {

        loadCart(context);

        for (FoodItem cartItem : cartList) {
            if (cartItem.getName().equals(item.getName())) {
                cartItem.increaseQuantity();
                saveCart(context);
                return;
            }
        }

        item.setQuantity(1);
        cartList.add(item);
        saveCart(context);
    }

    // ✅ Get cart items
    public static List<FoodItem> getCartItems(Context context) {
        loadCart(context);
        return cartList;
    }

    // ✅ Clear cart
    public static void clearCart(Context context) {
        cartList.clear();
        saveCart(context);
    }

    // ✅ Total amount calculation
    public static int getTotalAmount(Context context) {
        loadCart(context);

        int total = 0;
        for (FoodItem item : cartList) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    // ✅ Save cart to SharedPreferences
    public static void saveCart(Context context) {

        SharedPreferences prefs =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(cartList);

        editor.putString(KEY_CART, json);
        editor.apply();
    }

    // ✅ Load cart from SharedPreferences
    private static void loadCart(Context context) {

        SharedPreferences prefs =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = prefs.getString(KEY_CART, null);

        Type type = new TypeToken<ArrayList<FoodItem>>() {}.getType();

        if (json != null) {
            cartList = gson.fromJson(json, type);
        }
    }
}