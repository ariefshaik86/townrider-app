package com.example.myapplication;

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

public class BestReviewedAdapter
        extends RecyclerView.Adapter<BestReviewedAdapter.ViewHolder> {

    private List<BestReviewed> list;
    private static final String IMAGE_BASE =
            "http://10.177.237.34/townride/images/";

    public BestReviewedAdapter(List<BestReviewed> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_best_reviewed, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        BestReviewed item = list.get(position);

        holder.name.setText(item.getName());
        holder.price.setText("₹ " + item.getPrice());
        holder.rating.setText("⭐ " + item.getRating());

        Glide.with(holder.itemView.getContext())
                .load(IMAGE_BASE + item.getImage())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, price, rating;
        Button btnAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imgBest);
            name = itemView.findViewById(R.id.tvBestName);
            price = itemView.findViewById(R.id.tvBestPrice);
            rating = itemView.findViewById(R.id.tvBestRating);
            btnAdd = itemView.findViewById(R.id.btnBestAdd);
        }
    }
}