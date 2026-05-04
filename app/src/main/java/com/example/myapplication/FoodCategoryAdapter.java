package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FoodCategoryAdapter extends RecyclerView.Adapter<FoodCategoryAdapter.ViewHolder> {

    List<FoodCategory> list;
    OnCategoryClick listener;
    int selectedPosition = -1;

    private static final String BASE_URL =
            "http://10.177.237.34/townride/images/";

    public interface OnCategoryClick {
        void onClick(int categoryId);
    }

    public FoodCategoryAdapter(List<FoodCategory> list, OnCategoryClick listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FoodCategory category = list.get(position);

        holder.title.setText(category.getName());

        // ✅ LOAD IMAGE FROM SERVER
        String imageUrl = BASE_URL + category.getImage();

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.image);

        // ✅ Highlight selected category
        if (selectedPosition == position) {
            holder.title.setTextColor(0xFF2ECC71);
            holder.title.setTextSize(15);
        } else {
            holder.title.setTextColor(0xFF111111);
            holder.title.setTextSize(13);
        }

        holder.itemView.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
            listener.onClick(category.getId());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvFoodCategory);
            image = itemView.findViewById(R.id.imgFoodCategory);
        }
    }
}