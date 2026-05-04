package com.example.myapplication;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class GroceryOfferAdapter
        extends RecyclerView.Adapter<GroceryOfferAdapter.ViewHolder> {

    private List<GroceryOffer> list;

    private static final String BASE_URL =
            "http://10.177.237.34/townride/images/";

    public GroceryOfferAdapter(List<GroceryOffer> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grocery_offer, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        GroceryOffer item = list.get(position);

        holder.name.setText(item.getName());
        holder.originalPrice.setText("₹ " + item.getOriginalPrice());
        holder.offerPrice.setText("₹ " + item.getOfferPrice());

        // Strike original price
        holder.originalPrice.setPaintFlags(
                holder.originalPrice.getPaintFlags()
                        | Paint.STRIKE_THRU_TEXT_FLAG
        );

        // Calculate discount percentage
        try {
            int original = Integer.parseInt(item.getOriginalPrice());
            int offer = Integer.parseInt(item.getOfferPrice());

            int discount = ((original - offer) * 100) / original;
            holder.discount.setText(discount + "% OFF");

        } catch (Exception e) {
            holder.discount.setText("");
        }

        // Load image from server
        Glide.with(holder.itemView.getContext())
                .load(BASE_URL + item.getImage())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, originalPrice, offerPrice, discount;
        Button btnAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imgGroceryOffer);
            name = itemView.findViewById(R.id.tvGroceryOfferName);
            originalPrice = itemView.findViewById(R.id.tvOriginalPrice);
            offerPrice = itemView.findViewById(R.id.tvOfferPrice);
            discount = itemView.findViewById(R.id.tvDiscount);
            btnAdd = itemView.findViewById(R.id.btnAddGroceryOffer);
        }
    }
}