package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class GroceryBannerAdapter extends RecyclerView.Adapter<GroceryBannerAdapter.ViewHolder> {

    private List<String> bannerList;

    private static final String BASE_URL =
            "http://10.177.237.34/townride/images/";

    public GroceryBannerAdapter(List<String> bannerList) {
        this.bannerList = bannerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grocery_banner, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(holder.itemView.getContext())
                .load(BASE_URL + bannerList.get(position))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgBanner);
        }
    }
}