package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartActivity extends AppCompatActivity
        implements CartAdapter.CartUpdateListener {

    RecyclerView recyclerCart;
    TextView tvSubtotal, tvDelivery, tvGST, tvTotal;
    Button btnPlaceOrder;

    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerCart = findViewById(R.id.recyclerCart);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvDelivery = findViewById(R.id.tvDelivery);
        tvGST = findViewById(R.id.tvGST);
        tvTotal = findViewById(R.id.tvTotal);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        recyclerCart.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CartAdapter(
                CartManager.getCartItems(this),
                this,
                this
        );

        recyclerCart.setAdapter(adapter);

        updateTotal();
    }

    @Override
    public void onCartUpdated() {
        updateTotal();
        CartManager.saveCart(this);
    }

    private void updateTotal() {

        int subtotal = CartManager.getTotalAmount(this);

        int delivery;

        // Free delivery condition
        if (subtotal >= 499) {
            delivery = 0;
            tvDelivery.setText("Delivery: FREE 🎉");
            tvDelivery.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            delivery = 40;
            tvDelivery.setText("Delivery: ₹ " + delivery);
        }

        double gst = subtotal * 0.05;  // 5% GST
        double total = subtotal + delivery + gst;

        tvSubtotal.setText("Subtotal: ₹ " + subtotal);
        tvGST.setText("GST (5%): ₹ " + (int) gst);
        tvTotal.setText("Total: ₹ " + (int) total);
    }
}