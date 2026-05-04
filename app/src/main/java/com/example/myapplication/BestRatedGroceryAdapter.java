package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BestRatedGroceryAdapter
        extends RecyclerView.Adapter<BestRatedGroceryAdapter.ViewHolder> {

    private List<BestRatedGrocery> list;

//    private static final String BASE_URL =
//            "http://10.177.237.34/townride/images/";
private static final String BASE_URL =
        "http://10.177.237.34/townride/images/";

    public BestRatedGroceryAdapter(List<BestRatedGrocery> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_best_rated_grocery, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        BestRatedGrocery item = list.get(position);

        holder.name.setText(item.getName());
        holder.price.setText("₹ " + item.getPrice());
        holder.rating.setText("⭐ " + item.getRating());

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
        TextView name, price, rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imgBestGrocery);
            name = itemView.findViewById(R.id.tvBestGroceryName);
            price = itemView.findViewById(R.id.tvBestGroceryPrice);
            rating = itemView.findViewById(R.id.tvRating);
        }
    }
}